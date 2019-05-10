package sk.fri.uniza.db;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import sk.fri.uniza.api.Paged;
import sk.fri.uniza.api.Person;
import sk.fri.uniza.auth.Role;
import sk.fri.uniza.core.PersonBuilder;
import sk.fri.uniza.core.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UsersDao extends AbstractDAO<User> implements BasicDao<User, Long> {

    private static final List<Person> userDB;
    private static UsersDao userDao;

    static {
        Person user1 = new PersonBuilder()
                .setUserName("user@gmail.com")
                .setRoles(Set.of(Role.USER_READ_ONLY))
                .setPassword("heslo")
                .setFirstName("Adam")
                .setLastName("Sangala")
                .setEmail("user@gmail.com")
                .createPerson();

        Person user2 = new PersonBuilder()
                .setUserName("admin@gmail.com")
                .setRoles(Set.of(Role.ADMIN))
                .setPassword("heslo")
                .setFirstName("Jozko")
                .setLastName("Mrkvicka")
                .setEmail("admin@gmail.com")
                .createPerson();

        userDB = Stream.generate(() -> new PersonBuilder()
                .setUserName(UUID.randomUUID().toString())
                .setRoles(Set.of(Role.USER_READ_ONLY))
                .setPassword("heslo")
                .setFirstName("First Name")
                .setLastName("Last Name")
                .setEmail("Email@mail.com")
                .createPerson())
                .limit(100)
                .collect(Collectors.toList());
        userDB.add(user1);
        userDB.add(user2);
    }

    private UsersDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public static UsersDao getUserDao() {
        return userDao;
    }

    public static List<Person> getUserDB() {
        return userDB;
    }

    public static UsersDao createUsersDao(SessionFactory sessionFactory) {
        if (userDao == null)
            userDao = new UsersDao(sessionFactory);
        return userDao;
    }


    @Override
    public Optional<User> findById(Long id) {
        User user = get(id);
        return Optional.ofNullable(user);
    }

    public Optional<User> findByUsername(String username) {
        CriteriaBuilder cb = currentSession().getCriteriaBuilder();
        CriteriaQuery<User> cr = criteriaQuery();
        Root<User> root = cr.from(User.class);
        CriteriaQuery<User> userNameCr = cr.select(root).where(cb.like(root.get("userName"), username));
        return Optional.ofNullable(uniqueResult(userNameCr));
    }


    @Override
    public List<User> getAll() {
        return list(namedQuery("sk.fri.uniza.core.getAll"));
    }

    /**
     * https://www.baeldung.com/hibernate-pagination
     *
     * @param limit
     * @param page
     * @return
     */
    @Override
    public Paged<List<User>> getAll(int limit, int page) {

        String countQ = "Select count (f.id) from User f";
        Query countQuery = currentSession().createQuery(countQ);
        Long countResults = (Long) countQuery.uniqueResult();

        int lastPageNumber = (int) (Math.ceil((float) countResults / limit));

        Query selectQuery = query("FROM User ");
        selectQuery.setFirstResult((page - 1) * limit);
        selectQuery.setMaxResults(limit);
        List<User> usersPage = list(selectQuery);

        return new Paged<List<User>>(page, limit, countResults, usersPage);
    }

    @Override
    public Long save(User user) {
        return persist(user).getId();
    }

    @Override
    public Long update(User user, String[] params) {
        persist(user);
        return user.getId();

    }

    public void saveNewPassword(Long id, String password){
        User user = get(id);
        user.setUserName("test@test.sk");
//        user.setNewPassword(password);


    }

    @Override
    public void delete(User user) {
        currentSession().delete(user);

    }
}
