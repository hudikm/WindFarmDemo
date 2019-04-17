package sk.fri.uniza.auth;

import io.dropwizard.auth.Authorizer;

public class OAuth2Authorizer implements Authorizer<User> {
    @Override
    public boolean authorize(User user, String role) {
        return user.getRoles().contains(role);
    }
}
