package sk.fri.uniza.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.ArrayUtils;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.Set;

@Entity
@Table(name = "UsersDao")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQueries(
        {
                @NamedQuery(
                        name = "sk.fri.uniza.core.getAll",
                        query = "SELECT p FROM User p"
                )
        })

public class User implements Principal {
    private static Random rand = new Random((new Date()).getTime());
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private Long id;
    @JsonProperty
    private String userName;
    @Column
    @ElementCollection(targetClass = String.class)
    @JsonProperty
    private Set<String> roles;
    @JsonIgnore
    private byte[] secrete;
    @JsonIgnore
    private byte[] salt = new byte[8];

    // Hibernate need default constructor
    public User() {
    }
    public User(Long id, String userName, Set<String> roles, String password) {
        this.id = id;
        this.userName = userName;
        this.roles = roles;
        if (password != null)
            setNewPassword(password);
    }

    public User(String userName, Set<String> roles, String password) {
        this.userName = userName;
        this.roles = roles;
        setNewPassword(password);
    }

    /**
     * Return MD5 hashed password with salt.
     *
     * @param salt     byte array of random generater salt
     * @param password String representing password
     * @return MD5 hashed password with salt
     * @throws NoSuchAlgorithmException
     */

    public static byte[] generateHashSecrete(byte[] salt, String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        return md.digest(ArrayUtils.addAll(salt, password.getBytes()));
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /*
      Implement Principal interface
     */
    @JsonProperty("userName")
    @Override
    public String getName() {
        return userName;
    }

    public Long getId() {
        return id;
    }

    @Column
    @ElementCollection(targetClass = String.class)
    public Set<String> getRoles() {
        return roles;
    }

    @JsonIgnore
    public String getRolesString() {
        return String.join(",", roles);
    }

    public void setNewPassword(String newPassword) {
        rand.nextBytes(salt);
        secrete = User.generateHashSecrete(salt, newPassword);
    }

    /**
     * Test if entered password is correct
     */
    public boolean testPassword(String password) {
        byte[] bytes = generateHashSecrete(this.salt, password);
        return Arrays.equals(bytes, secrete);

    }
}
