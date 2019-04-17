package sk.fri.uniza.api;

import javax.ws.rs.QueryParam;


public class OauthRequest {

    @QueryParam("client_id")
    private String clientId;
    @QueryParam("scope")
    private String scope;
    @QueryParam("state")
    private String state;
    @QueryParam("redirect_uri")
    private String redirectUri;
    @QueryParam("response_type")
    private String responseType;

    @QueryParam("client_id")
    public String getClientId() {
        return clientId;
    }

    @QueryParam("client_id")
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @QueryParam("scope")
    public String getScope() {
        return scope;
    }

    @QueryParam("scope")
    public void setScope(String scope) {
        this.scope = scope;
    }

    @QueryParam("state")
    public String getState() {
        return state;
    }

    @QueryParam("state")
    public void setState(String state) {
        this.state = state;
    }

    @QueryParam("redirect_uri")
    public String getRedirectUri() {
        return redirectUri;
    }

    @QueryParam("redirect_uri")
    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    @QueryParam("response_type")
    public String getResponseType() {
        return responseType;
    }

    @QueryParam("response_type")
    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

}