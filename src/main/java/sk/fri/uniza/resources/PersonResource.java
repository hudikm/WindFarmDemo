package sk.fri.uniza.resources;

import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.fri.uniza.api.Paged;
import sk.fri.uniza.api.Person;
import sk.fri.uniza.auth.Role;
import sk.fri.uniza.core.User;
import sk.fri.uniza.db.PersonDao;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/persons")
public class PersonResource {
    private final Logger myLogger = LoggerFactory.getLogger(this.getClass());
    private PersonDao personDao;

    public PersonResource(PersonDao personDao) {
        this.personDao = personDao;
    }

    @GET
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(Role.ADMIN)
    public Response getListOfPersons(@QueryParam("limit") Integer limit, @QueryParam("page") Integer page) {
        if (page == null) page = 1;
        if (limit != null) {
            Paged<List<Person>> listPaged = personDao.getAll(limit, page);
            return Response.ok()
                    .entity(listPaged)
                    .build();

        } else {
            List<Person> userList = personDao.getAll();
            return Response.ok()
                    .entity(userList)
                    .build();
        }

    }
}
