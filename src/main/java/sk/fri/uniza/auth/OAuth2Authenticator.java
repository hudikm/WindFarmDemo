package sk.fri.uniza.auth;

import com.google.common.collect.ImmutableSet;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

public class OAuth2Authenticator implements Authenticator<String, User> {
    private static final Logger LOG = LoggerFactory.getLogger(OAuth2Authenticator.class);

    private Key key;

    public OAuth2Authenticator(Key key) {
        //We will sign our JWT with our ApiKey secret
        // this.key = new SecretKeySpec(key.getEncoded(), SignatureAlgorithm.forSigningKey(key).getJcaName());
        this.key = key;

    }

    @Override
    public Optional<User> authenticate(String jwtToken) throws AuthenticationException {
        Claims claimsJws;

        try {
            claimsJws = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(jwtToken)
                    .getBody();
            //OK, we can trust this JWT
        } catch (SignatureException e) {
            //don't trust the JWT!
            LOG.error("Failed to authenticate token!", e);
            throw new AuthenticationException("Failed to authenticate token!", e);
        }

        Set<String> roles = parseRolesClaim(claimsJws);

        return Optional.of(
                User.newBuilder()
                        .withId(claimsJws.getSubject())
                        .withUUID(claimsJws.getId())
                        .withRoles(roles)
                        .build());
    }


    private Set<String> parseRolesClaim(Claims claims) throws AuthenticationException {
        Object scopesObject = claims.get("scope", String.class);
        String[] scopes = {};
        if (scopesObject != null && scopesObject instanceof String) {
            scopes = ((String) scopesObject).split(" ");
        }
        return ImmutableSet.copyOf(Arrays.asList(scopes));
    }

}
