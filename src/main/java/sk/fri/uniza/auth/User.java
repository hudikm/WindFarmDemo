package sk.fri.uniza.auth;

import org.apache.commons.lang.ArrayUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.Set;

public final class User implements Principal {

    private final String id;
    private final String UUID;
    private final Set<String> roles;
    private byte[] secrete;
    private byte[] salt = new byte[8];

    public static byte[] generateHashSecrete(byte[] salt, String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return md.digest(ArrayUtils.addAll(salt, password.getBytes()));
    }

    /*
     * Constructors
     */

    private User(Builder builder) {
        id = builder.id;
        UUID = builder.orgId;
        roles = builder.roles;
        salt = builder.salt;
        secrete = builder.secrete;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(User copy) {
        Builder builder = new Builder();
        builder.id = copy.id;
        builder.orgId = copy.UUID;
        builder.roles = copy.roles;
        builder.salt = copy.salt;
        builder.secrete = copy.secrete;
        return builder;
    }

    /*
     * Implement Principal interface
     */

    @Override
    public String getName() {
        return id;
    }

    /*
     * Getters
     */
    public String getId() {
        return id;
    }

    public String getUUID() {
        return UUID;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public String getRolesString() {
        return String.join(",", roles);
    }

    /*
     * Test if entered password is correct
     */
    public boolean testPassword(String password) {

        try {
            byte[] bytes = generateHashSecrete(this.salt, password);

            return Arrays.equals(bytes, secrete);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
     * Builder
     */

    public static final class Builder {
        private static Random rand = new Random((new Date()).getTime());
        private String id;
        private String orgId;
        private Set<String> roles;
        private byte[] salt = new byte[8];
        private byte[] secrete;

        private Builder() {
        }

        public Builder withId(String val) {
            id = val;
            return this;
        }

        public Builder withUUID(String val) {
            orgId = val;
            return this;
        }

        public Builder withRoles(Set<String> val) {
            roles = val;
            return this;
        }

        public Builder withPassword(String password) {
            rand.nextBytes(salt);
            try {
                secrete = User.generateHashSecrete(salt, password);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
