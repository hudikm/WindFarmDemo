package sk.fri.uniza.resources;

import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.fri.uniza.api.Paged;
import sk.fri.uniza.core.User;
import sk.fri.uniza.db.UsersDao;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/users")
public class UsersResource {
    private final Logger myLogger = LoggerFactory.getLogger(this.getClass());
    private UsersDao usersDao;

    public UsersResource(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    @GET
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getListOfUsers(@QueryParam("limit") Integer limit, @QueryParam("page") Integer page) {
        if (page == null) page = 1;
        if (limit != null) {
            Paged<List<User>> listPaged = usersDao.getAll(limit, page);
            return Response.ok()
                    .entity(listPaged)
                    .build();

        } else {
            List<User> userList = usersDao.getAll();
            return Response.ok()
                    .entity(userList)
                    .build();
        }

    }
}
