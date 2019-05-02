package sk.fri.uniza.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.hibernate.validator.constraints.NotEmpty;

import javax.crypto.SecretKey;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

public class OAuth2Configuration {
    @NotEmpty
    private String keyAlias;

    @NotEmpty
    private String keystorePassword;
    @NotEmpty
    private String keyPassword;
    @NotEmpty
    private String keystoreFilename;

    @JsonProperty
    public String getKeyAlias() {
        return keyAlias;
    }

    @JsonProperty
    public void setKeyAlias(String keyAlias) {
        this.keyAlias = keyAlias;
    }

    @JsonProperty
    public String getKeystorePassword() {
        return keystorePassword;
    }

    @JsonProperty
    public void setKeystorePassword(String keystorePassword) {
        this.keystorePassword = keystorePassword;
    }

    @JsonProperty
    public String getKeyPassword() {
        return keyPassword;
    }

    @JsonProperty
    public void setKeyPassword(String keyPassword) {
        this.keyPassword = keyPassword;
    }

    @JsonProperty
    public String getKeystoreFilename() {
        return keystoreFilename;
    }

    @JsonProperty
    public void setKeystoreFilename(String keystoreFilename) {
        this.keystoreFilename = keystoreFilename;
    }


    /**
     * Read and return KeyPair from keystore file. Keystore file is defined in config.yml file.
     *
     * @param createIfNotExist create new key if there is no key present in keystore
     * @return Key pair (public, private)
     */
    public KeyPair getSecreteKey(boolean createIfNotExist) {
        KeyStore ks = null;
        Key key = null;
        PublicKey publicKey = null;

        try {
            ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(new FileInputStream(keystoreFilename), keystorePassword.toCharArray());
            key = ks.getKey(keyAlias, keyPassword.toCharArray());

            if (key instanceof PrivateKey) {

                // Get certificate of public key
                Certificate cert = ks.getCertificate(keyAlias);

                // Get public key
                publicKey = cert.getPublicKey();

            }

            // Return a key pair
            return new KeyPair(publicKey, (PrivateKey) key);

        } catch (FileNotFoundException e) {

        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException | UnrecoverableEntryException e) {
            e.printStackTrace();
        }

        // If key was not found generate new key

        if (key == null && createIfNotExist) {
            assert (ks != null);
            // Generate new secret key
            SecretKey newKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            KeyStore.SecretKeyEntry secret = new KeyStore.SecretKeyEntry(newKey);
            KeyStore.ProtectionParameter password = new KeyStore.PasswordProtection("".toCharArray());

            try {
                ks.load(null, null);
                ks.setEntry(keyAlias, secret, password);

                FileOutputStream fos = new FileOutputStream(keystoreFilename);
                ks.store(fos, keystorePassword.toCharArray());

                // Return a key pair
                return new KeyPair(null, (PrivateKey) key);

            } catch (KeyStoreException | IOException | CertificateException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}
