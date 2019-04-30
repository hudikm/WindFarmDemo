package sk.fri.uniza.resources;

import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.hibernate.FlushMode;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.fri.uniza.api.Paged;
import sk.fri.uniza.api.Person;
import sk.fri.uniza.auth.Role;
import sk.fri.uniza.core.User;
import sk.fri.uniza.db.PersonDao;
import sk.fri.uniza.db.UsersDao;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

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

    @DELETE
    @UnitOfWork
    @RolesAllowed(Role.ADMIN)
    public Response deletePerson(@Auth User user,@QueryParam("id") Long id) {
        if (user.getId() != id) {
            Optional<Person> person1 = personDao.findById(id);
//            person1.ifPresent(person -> {
//                personDao.delete(person);
//            });

            return person1.map(person -> {
                personDao.delete(person);
                return Response.ok().build();
            }).get();
//            return Response.ok().build();

        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({Role.ADMIN, Role.USER_READ_ONLY})
    public Person getPersonInfo(@Auth User user, @PathParam("id") Long id) {

        if (!user.getRoles().contains(Role.ADMIN)) {
            if (user.getId() != id) {
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            }
        }

        Optional<Person> person = personDao.findById(id);
        return person.orElseThrow(() -> {
            throw new WebApplicationException("Wrong person ID!", Response.Status.BAD_REQUEST);
        });

    }

    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({Role.ADMIN, Role.USER_READ_ONLY})
    public Person setPersonInfo(@Auth User user, @Valid Person person) {

        if (!user.getRoles().contains(Role.ADMIN)) {
            if (user.getId() != person.getId()) {
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            }
        }
        personDao.save(person);
        return person;
    }

    @POST
    @Path("/password")
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @RolesAllowed({Role.ADMIN})
    public Response setNewPassword(@QueryParam("id") Long id, @FormParam("password") String password) {

        personDao.saveNewPassword(id, password);
        return Response.ok().build();


    }

}
