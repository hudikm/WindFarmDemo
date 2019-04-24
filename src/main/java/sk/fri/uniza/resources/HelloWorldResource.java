package sk.fri.uniza.resources;


import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.sessions.Session;
import sk.fri.uniza.api.Saying;
import sk.fri.uniza.auth.Role;
import sk.fri.uniza.core.User;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Optional;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldResource {
    private final String template;
    private final String defaultName;
    private final AtomicLong counter;

    public HelloWorldResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
    }

    @GET
    @Path("/hello-world")
    @Timed
    @RolesAllowed(Role.ADMIN)
    public Saying sayHello(@QueryParam("name") Optional<String> name) {
        final String value = String.format(template, name.orElse(defaultName));
        return new Saying(counter.incrementAndGet(), value);
    }

    @GET
    @Path("/hello-user")
    @Timed
    @RolesAllowed({Role.ADMIN, Role.USER_READ_ONLY})
    public Saying sayHello(@Auth User user) {
        final String value = String.format(template, user.getName());
        return new Saying(counter.incrementAndGet(), value);
    }

    @GET
    @Path("/sessiontest")
    @Timed
    @ExceptionMetered
    public Response getMain(@Context UriInfo info, @Session HttpSession session) {

        return Response.ok().build();
    }
}