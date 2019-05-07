package sk.fri.uniza.core;

import sk.fri.uniza.api.Person;

import java.util.Set;

public class PersonBuilder {
    private String userName;
    private Set<String> roles;
    private String password;
    private String firstName;
    private String lastName;
    private String email;


    public PersonBuilder setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public PersonBuilder setRoles(Set<String> roles) {
        this.roles = roles;
        return this;
    }

    public PersonBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public PersonBuilder setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public PersonBuilder setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public PersonBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public Person createPerson() {
        return new Person(userName, roles, password, firstName, lastName, email);
    }
}