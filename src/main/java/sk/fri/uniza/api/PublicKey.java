package sk.fri.uniza.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "pubKey",
        "keyFormat",
        "keyAlg"

})
public class PublicKey {


    @JsonProperty("pubKey")
    private String pubKey;
    @JsonProperty("keyFormat")
    private String keyFormat;
    @JsonProperty("keyAlg")
    private String keyAlg;

    public PublicKey(Key key) {
        this.pubKey = Base64.getEncoder().encodeToString(key.getEncoded());
        this.keyFormat = key.getFormat();
        this.keyAlg = key.getAlgorithm();
    }

    public PublicKey() {
    }


    @JsonProperty("keyAlg")
    public String getKeyAlg() {
        return keyAlg;
    }

    @JsonProperty("keyAlg")
    public void setKeyAlg(String keyAlg) {
        this.keyAlg = keyAlg;
    }

    @JsonProperty("pubKey")
    public String getPubKey() {
        return pubKey;
    }

    @JsonProperty("pubKey")
    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }

    @JsonProperty("keyFormat")
    public String getKeyFormat() {
        return keyFormat;
    }

    @JsonProperty("keyFormat")
    public void setKeyFormat(String keyFormat) {
        this.keyFormat = keyFormat;
    }

    @JsonIgnore
    public Key getKeyInstance() throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] decoded = Base64.getDecoder().decode(pubKey);
        X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance(keyAlg);//RSA
        return keyFactory.generatePublic(bobPubKeySpec);
    }

}