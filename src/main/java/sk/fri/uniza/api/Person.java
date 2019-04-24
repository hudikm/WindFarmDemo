package sk.fri.uniza.api;

import sk.fri.uniza.core.User;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Set;

@Entity
@DiscriminatorValue("Person")
public class Person extends User {
    private String FirstName;
    private String LastName;
    private String email;

    public Person() {

    }

    public Person(String userName, Set<String> roles, String password, String firstName, String lastName, String email) {
        super(userName, roles, password);
        FirstName = firstName;
        LastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
