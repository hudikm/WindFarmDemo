package sk.fri.uniza.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.fri.uniza.api.*;
import sk.fri.uniza.auth.OAuth2Clients;
import sk.fri.uniza.core.User;
import sk.fri.uniza.db.UsersDao;
import sk.fri.uniza.views.LoginPageView;
import sk.fri.uniza.views.LoginPageViewBuilder;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.security.Key;
import java.security.KeyPair;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Path("/login")
@Api(value = "OAuth2 and Login")
public class LoginResource {

    private final Logger myLogger = LoggerFactory.getLogger(this.getClass());
    private final ConcurrentHashMap<String, OauthRequest> oauthSession;
    private final ConcurrentHashMap<String, User> oauthCodes;


    private Key privateKey;
    private Key publicKey;
    private UsersDao usersDao;
    private Claims claimsJws;
    private OAuth2Clients oAuth2Clients;

    public LoginResource(KeyPair keyPair, UsersDao usersDao, OAuth2Clients oAuth2Clients) {
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
        this.usersDao = usersDao;
        this.oAuth2Clients = oAuth2Clients;
        oauthSession = new ConcurrentHashMap<String, OauthRequest>();
        oauthCodes = new ConcurrentHashMap<String, User>();

    }


    @GET
    @Produces(MediaType.TEXT_HTML)
    @ApiOperation(value = "Display Login page")
    public LoginPageView showLoginPage(@BeanParam OauthRequest oauthRequest) {

        return oAuth2Clients.findById(oauthRequest.getClientId())
                .map(oAuth2Client -> {

                    String sessionId = UUID.randomUUID().toString();
                    oauthSession.put(sessionId, oauthRequest);

                    switch (oauthRequest.getResponseType()) {
                        case "code":
                            return new LoginPageViewBuilder().setRootPath("../../").setSessionId(sessionId).setAction("/api/login/code").createLoginPageView();

                        case "token":
                            return new LoginPageViewBuilder().setRootPath("../../").setSessionId(sessionId).setAction("/api/login/implicit").createLoginPageView();

                        default:
                            throw new WebApplicationException("Auth2 bad response type. Support only \"code\" or \"token\"", Response.Status.BAD_REQUEST);
                    }
                })
                .orElseGet(() -> {
                    throw new WebApplicationException("Client id not found! Check your client id", Response.Status.NOT_FOUND);
                });

    }


    @Path("/code")
    @POST
    @UnitOfWork
    @ApiOperation(value = "Request to obtain code")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_FORM_URLENCODED)
    public Response getAccessCode(@NotNull @BeanParam LoginData formParams) {
        if (formParams != null) {
            /*
              Test if login info is valid i.e. User name and password
             */
            Optional<User> optionalUser = usersDao.findByUsername(formParams.getUsername());
            if (!optionalUser.isPresent()) {
                throw new WebApplicationException("<div class=\"red lighten-2\" style=\"padding:8px;\"><b>Autentifikácia zlyhala.<b/> <br>" +
                        "Skontrolujte prihlasovacie meno a heslo.</div> <br> <a class=\"waves-effect waves-light btn orange\" href=\"javascript:history.back()\">Späť</a>", Response.Status.UNAUTHORIZED);
            }

            User user = optionalUser.get();
            if (!user.testPassword(formParams.getPassword())) {
                throw new WebApplicationException("<div class=\"red lighten-2\" style=\"padding:8px;\"><b>Autentifikácia zlyhala.<b/> <br>" +
                        "Skontrolujte prihlasovacie meno a heslo.</div> <br> <a class=\"waves-effect waves-light btn orange\" href=\"javascript:history.back()\">Späť</a>", Response.Status.UNAUTHORIZED);
            }

            OauthRequest oauthRequest = oauthSession.remove(formParams.getSessionId());
            String code = UUID.randomUUID().toString();

            oauthCodes.put(code, user);

            URI uri = UriBuilder.fromPath(oauthRequest.getRedirectUri())
                    .queryParam("code", code)
                    .queryParam("state", oauthRequest.getState())
                    .queryParam("stay_signin", formParams.getStaySignin() != null)
                    .build();

//            return Response.status(Response.Status.TEMPORARY_REDIRECT)
//                    .location(uri)
//                    .build();
            return Response.seeOther(uri).build();
        }
        throw new WebApplicationException("<div class=\"red lighten-2\" style=\"padding:8px;\"><b>Autentifikácia zlyhala.<b/> <br>" +
                "Skontrolujte prihlasovacie meno a heslo.</div> <br> <a class=\"waves-effect waves-light btn orange\" href=\"javascript:history.back()\">Späť</a>", Response.Status.UNAUTHORIZED);
    }

    @Path("/implicit")
    @POST
    @UnitOfWork
    @ApiOperation(value = "Request to obtain access token. Implicit flow")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response implicitFlowAuth(@NotNull @BeanParam LoginData formParams) {
        if (formParams != null) {

            /*
              Test if login info is valid i.e. User name and password
             */
            Optional<User> optionalUser = usersDao.findByUsername(formParams.getUsername());
            if (!optionalUser.isPresent()) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            User user = optionalUser.get();
            if (!user.testPassword(formParams.getPassword())) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            OauthRequest oauthRequest = oauthSession.remove(formParams.getSessionId());

            AccessToken accessToken = getAccessToken(user);

            URI uri = UriBuilder.fromPath(oauthRequest.getRedirectUri())
                    .fragment("access_token=" + accessToken.getAccessToken()
                            + "&token_type=" + accessToken.getTokenType()
                            + "&expires_in=" + accessToken.getExpiresIn()
                            + "&state=" + oauthRequest.getState())
                    .build();


            CacheControl cacheControl = new CacheControl();
            cacheControl.setNoStore(true);
            cacheControl.setNoCache(true);

            return Response.seeOther(uri)
                    .cacheControl(cacheControl)
                    .type(MediaType.APPLICATION_FORM_URLENCODED)
                    .build();

//            return Response.status(Response.Status.TEMPORARY_REDIRECT)
//                    .cacheControl(cacheControl)
//                    .type(MediaType.APPLICATION_FORM_URLENCODED)
//                    .location(uri)
//                    .build();
        }
        return Response.noContent().build();
    }

    @POST
    @Path("/token")
    @ApiOperation(value = "Request to obtain access token from code. Authorization Code")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getAccessToken(@NotNull @BeanParam OauthTokenRequest tokenRequest) {

        oAuth2Clients.findById(tokenRequest.getClientId())
                .filter(oAuth2Client -> oAuth2Client.getClientSecrete().equals(tokenRequest.getClient_secret()))
                .orElseGet(() -> {
                    throw new WebApplicationException(Response.Status.NOT_FOUND);
                });

        User user = oauthCodes.remove(tokenRequest.getCode());
        user = usersDao.findById(user.getId()).orElseThrow(() -> {
            throw new WebApplicationException("<div class=\"red lighten-2\" style=\"padding:8px;\"><b>Autentifikácia zlyhala.<b/> <br>" +
                    "Skontrolujte prihlasovacie meno a heslo.</div> <br> <a class=\"waves-effect waves-light btn orange\" href=\"javascript:history.back()\">Späť</a>", Response.Status.UNAUTHORIZED);
        });

        return getTokenResponse(user);
    }


    private Response getTokenResponse(User user) {
        AccessToken accessToken = getAccessToken(user);

        CacheControl cacheControl = new CacheControl();
        cacheControl.setNoStore(true);
        cacheControl.setNoCache(true);

        return Response.status(Response.Status.OK)
                .cacheControl(cacheControl)
                .type(MediaType.APPLICATION_JSON)
                .entity(accessToken)
                .build();
    }

    private AccessToken getAccessToken(User user) {
        String jwt = createJWT(user.getId(), "me", user.getName(), Integer.MAX_VALUE, Map.of("scope", user.getRolesString()));

        return new AccessToken().withAccessToken(jwt)
                .withTokenType("Bearer")
                .withExpiresIn(Integer.MAX_VALUE)
                .withRefreshToken("tGzv3JOkF0XG5Qx2TlKWIA")
                .withExampleParameter("example_value");
    }

    public String createJWT(Long id, String issuer, String subject, long ttlMillis, Map<String, Object> claims) {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setId(id.toString())
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(privateKey);

        //if it has been specified, let's add the expiration
        if (ttlMillis > 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        if (claims != null) {
            builder.addClaims(claims);
        }
        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    @GET
    @Path("/public-key")
    @ApiOperation(value = "Request to obtain public key", notes = "An obtained public key can by used to verify JWT token")
    @Produces(MediaType.APPLICATION_JSON)
    public sk.fri.uniza.api.PublicKey getPublicKey() {
        sk.fri.uniza.api.PublicKey publicKey = new PublicKey(this.publicKey);
        return publicKey;
    }


}

