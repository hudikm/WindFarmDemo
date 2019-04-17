package sk.fri.uniza.resources;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.fri.uniza.api.*;
import sk.fri.uniza.auth.OAuth2Clients;
import sk.fri.uniza.auth.User;
import sk.fri.uniza.auth.Users;
import sk.fri.uniza.views.LoginPageView;
import sk.fri.uniza.views.LoginPageViewBuilder;


import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;


import java.lang.annotation.Annotation;
import java.net.URI;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Path("/login")
public class LoginResource {

    private final Logger myLogger = LoggerFactory.getLogger(this.getClass());
    private final ConcurrentHashMap<String, OauthRequest> oauthSession;
    private final ConcurrentHashMap<String, User> oauthCodes;


    private Key privateKey;
    private Key publicKey;
    private Users users;
    private Claims claimsJws;
    private OAuth2Clients oAuth2Clients;

    public LoginResource(KeyPair keyPair, Users users, OAuth2Clients oAuth2Clients) {
        //We will sign our JWT with our ApiKey secret
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
//        this.privateKey = new SecretKeySpec(privateKey.getEncoded(), SignatureAlgorithm.forSigningKey(privateKey).getJcaName());
        this.users = users;
        this.oAuth2Clients = oAuth2Clients;
        oauthSession = new ConcurrentHashMap<String, OauthRequest>();
        oauthCodes = new ConcurrentHashMap<String, User>();

    }

    @Path("/")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public LoginPageView getLoginPage(@BeanParam OauthRequest oauthRequest) {

        return oAuth2Clients.get(oauthRequest.getClientId())
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
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_FORM_URLENCODED)
    public Response getAccsesCode(/*MultivaluedMap<String, String> */@NotNull @BeanParam LoginData formParams) {
        if (formParams != null) {

            /*
              Test if login info is valid i.e. User name and password
             */
            Optional<User> optionalUser = users.get(formParams.getUsername());
            if (!optionalUser.isPresent()) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            User user = optionalUser.get();
            if (!user.testPassword(formParams.getPassword())) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }


            OauthRequest oauthRequest = oauthSession.remove(formParams.getSessionId());
            String code = UUID.randomUUID().toString();

            oauthCodes.put(code, user);

            URI uri = UriBuilder.fromPath(oauthRequest.getRedirectUri())
                    .queryParam("code", code).build();

            return Response.status(Response.Status.TEMPORARY_REDIRECT)
                    .location(uri)
                    .build();
        }
        return Response.noContent().build();
    }

    @Path("/implicit")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//    @Produces(MediaType.APPLICATION_JSON)
    public Response getImplicitFlow(/*MultivaluedMap<String, String> */@NotNull @BeanParam LoginData formParams) {
        if (formParams != null) {

            /*
              Test if login info is valid i.e. User name and password
             */
            Optional<User> optionalUser = users.get(formParams.getUsername());
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

            return Response.status(Response.Status.TEMPORARY_REDIRECT)
                    .cacheControl(cacheControl)
                    .type(MediaType.APPLICATION_FORM_URLENCODED)
                    .location(uri)
                    .build();
        }
        return Response.noContent().build();
    }

    @Path("/token")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccessToken(/*MultivaluedMap<String, String> params*/@NotNull @BeanParam OauthTokenRequest tokenRequest) {

        oAuth2Clients.get(tokenRequest.getClientId())
                .filter(oAuth2Client -> oAuth2Client.getClientSecrete().equals(tokenRequest.getClient_secret()))
                .orElseGet(() -> {
                    throw new WebApplicationException(Response.Status.NOT_FOUND);
                });

        User user = oauthCodes.remove(tokenRequest.getCode());

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
        String jwt = createJWT(user.getUUID(), "me", user.getName(), Integer.MAX_VALUE, Map.of("scope", user.getRolesString()));

        return new AccessToken().withAccessToken(jwt)
                .withTokenType("Bearer")
                .withExpiresIn(Integer.MAX_VALUE)
                .withRefreshToken("tGzv3JOkF0XG5Qx2TlKWIA")
                .withExampleParameter("example_value");
    }

    public String createJWT(String id, String issuer, String subject, long ttlMillis, Map<String, Object> claims) {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);


        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setId(id)
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
    @Produces(MediaType.APPLICATION_JSON)
    public sk.fri.uniza.api.PublicKey getPublicKey() {
        sk.fri.uniza.api.PublicKey publicKey = new PublicKey(this.publicKey);
        return publicKey;
    }


}

