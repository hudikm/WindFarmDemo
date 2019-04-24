package sk.fri.uniza.core;

import java.util.Set;

public class UserBuilder {
    private Long id = null;
    private String userName;
    private Set<String> roles;
    private String password;

    public UserBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public UserBuilder setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public UserBuilder setRoles(Set<String> roles) {
        this.roles = roles;
        return this;
    }

    public UserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public User createUser() {
        if (id == null)
            return new User(userName, roles, password);
        else
            return new User(id, userName, roles, password);
    }
}