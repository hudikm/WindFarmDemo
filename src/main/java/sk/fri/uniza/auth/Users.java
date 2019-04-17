package sk.fri.uniza.auth;

import com.google.common.collect.ImmutableMap;
import sk.fri.uniza.db.BasicDao;
import java.util.*;

public class Users implements BasicDao<User, String> {

    private static final Map<String, User> userDB;

    static {
        User user1 = User.newBuilder()
                .withId("martin.hudik@fri.uniza.sk")
                .withRoles(Set.of(Role.ADMIN))
                .withUUID(UUID.randomUUID().toString())
                .withPassword("heslo")
                .build();
        User user2 = User.newBuilder()
                .withId("hudikm@gmail.com")
                .withRoles(Set.of(Role.USER_READ_ONLY))
                .withUUID(UUID.randomUUID().toString())
                .withPassword("heslo")
                .build();

        userDB = ImmutableMap.of(
                user1.getName(), user1,
                user2.getName(), user2
        );
    }

    @Override
    public Optional<User> get(String id) {
        User user = userDB.get(id);
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(userDB.values());
    }

    @Override
    public void save(User user) {

    }

    @Override
    public void update(User user, String[] params) {

    }

    @Override
    public void delete(User user) {

    }
}
