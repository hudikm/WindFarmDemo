package sk.fri.uniza.api;

import javax.ws.rs.FormParam;


public class OauthTokenRequest {

    @FormParam("code")
    private String code;
    @FormParam("grant_type")
    private String grant_type;
    @FormParam("client_secret")
    private String client_secret;
    @FormParam("redirect_uri")
    private String redirectUri;
    @FormParam("state")
    private String state;
    @FormParam("client_id")
    private String clientId;

    @FormParam("code")
    public String getCode() {
        return code;
    }

    @FormParam("code")
    public void setCode(String code) {
        this.code = code;
    }

    @FormParam("client_id")
    public String getClientId() {
        return clientId;
    }

    @FormParam("client_id")
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @FormParam("client_secret")
    public String getClient_secret() {
        return client_secret;
    }

    @FormParam("client_secret")
    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    @FormParam("state")
    public String getState() {
        return state;
    }

    @FormParam("state")
    public void setState(String state) {
        this.state = state;
    }

    @FormParam("redirect_uri")
    public String getRedirectUri() {
        return redirectUri;
    }

    @FormParam("redirect_uri")
    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    @FormParam("grant_type")
    public String getGrant_type() {
        return grant_type;
    }

    @FormParam("grant_type")
    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

}