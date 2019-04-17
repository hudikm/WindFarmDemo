package sk.fri.uniza.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "access_token",
        "token_type",
        "expires_in",
        "refresh_token",
        "example_parameter"
})
public class AccessToken {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("expires_in")
    private Integer expiresIn;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("example_parameter")
    private String exampleParameter;

    @JsonProperty("access_token")
    public String getAccessToken() {
        return accessToken;
    }

    @JsonProperty("access_token")
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public AccessToken withAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    @JsonProperty("token_type")
    public String getTokenType() {
        return tokenType;
    }

    @JsonProperty("token_type")
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public AccessToken withTokenType(String tokenType) {
        this.tokenType = tokenType;
        return this;
    }

    @JsonProperty("expires_in")
    public Integer getExpiresIn() {
        return expiresIn;
    }

    @JsonProperty("expires_in")
    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public AccessToken withExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    @JsonProperty("refresh_token")
    public String getRefreshToken() {
        return refreshToken;
    }

    @JsonProperty("refresh_token")
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public AccessToken withRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    @JsonProperty("example_parameter")
    public String getExampleParameter() {
        return exampleParameter;
    }

    @JsonProperty("example_parameter")
    public void setExampleParameter(String exampleParameter) {
        this.exampleParameter = exampleParameter;
    }

    public AccessToken withExampleParameter(String exampleParameter) {
        this.exampleParameter = exampleParameter;
        return this;
    }

}