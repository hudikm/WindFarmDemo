package sk.fri.uniza;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.bundles.redirect.HttpsRedirect;
import io.dropwizard.bundles.redirect.RedirectBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import org.eclipse.jetty.server.session.SessionHandler;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import sk.fri.uniza.auth.*;
import sk.fri.uniza.config.WindFarmDemoConfiguration;
import sk.fri.uniza.health.TemplateHealthCheck;
import sk.fri.uniza.resources.HelloWorldResource;
import sk.fri.uniza.resources.LoginResource;

import java.security.KeyPair;
import java.util.Map;

public class WindFarmDemoApplication extends Application<WindFarmDemoConfiguration> {

    public static void main(final String[] args) throws Exception {
        new WindFarmDemoApplication().run(args);
    }

    @Override
    public String getName() {
        return "WindFarmDemo";
    }

    @Override
    public void initialize(final Bootstrap<WindFarmDemoConfiguration> bootstrap) {

        bootstrap.addBundle(new AssetsBundle("/assets/", "/"));
        bootstrap.addBundle(new RedirectBundle(
                new HttpsRedirect()
        ));
        bootstrap.addBundle(new ViewBundle<WindFarmDemoConfiguration>() {
            @Override
            public Map<String, Map<String, String>> getViewConfiguration(WindFarmDemoConfiguration configuration) {
                return configuration.getViewRendererConfiguration();
            }
        });
    }

    @Override
    public void run(final WindFarmDemoConfiguration configuration,
                    final Environment environment) {


        //Add HealthChecks
        final TemplateHealthCheck templateHealthCheck = new TemplateHealthCheck(configuration.getTemplate());

        environment.healthChecks().register("templateHealthCheck", templateHealthCheck);

        // Register Resources
        registerResources(configuration, environment);

        // Setup user auth
        registerUserAuth(configuration, environment);

//        environment.jersey().register(HttpSessionProvider.class);
        SessionHandler sessionHandler = new SessionHandler();

        environment.servlets().setSessionHandler(sessionHandler);
    }

    private void registerUserAuth(WindFarmDemoConfiguration configuration, Environment environment) {
        KeyPair secreteKey = configuration.getoAuth2Configuration().getSecreteKey(false);
        environment.jersey().register(new AuthDynamicFeature(
                new OAuthCredentialAuthFilter.Builder<User>()
                        .setAuthenticator(new OAuth2Authenticator(secreteKey.getPublic()))
                        .setAuthorizer(new OAuth2Authorizer())
                        .setPrefix("Bearer")
                        .buildAuthFilter()
        ));

        // Enable the resource protection annotations: @RolesAllowed, @PermitAll & @DenyAll
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        // Enable the @Auth annotation for binding authenticated users to resource method parameters
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
    }

    private void registerResources(WindFarmDemoConfiguration configuration, Environment environment) {
        final Users users = new Users();
        final HelloWorldResource helloWorldResource = new HelloWorldResource(configuration.getTemplate(), configuration.getDefaultName());

        KeyPair secreteKey = configuration.getoAuth2Configuration().getSecreteKey(false);
        final LoginResource loginResource = new LoginResource(secreteKey, users, OAuth2Clients.getInstance());

        environment.jersey().register(helloWorldResource);
        environment.jersey().register(loginResource);
    }

}

