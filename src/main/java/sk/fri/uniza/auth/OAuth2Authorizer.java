package sk.fri.uniza.auth;

import io.dropwizard.auth.Authorizer;
import sk.fri.uniza.core.User;

public class OAuth2Authorizer implements Authorizer<User> {
    @Override
    public boolean authorize(User user, String role) {
        return user.getRoles().contains(role);
    }
}
