package sk.fri.uniza.api;

import io.swagger.annotations.ApiParam;

import javax.ws.rs.QueryParam;


public class OauthRequest {

    @ApiParam(value = "The id of the application that asks for authorization", required = true)
    @QueryParam("client_id")
    private String clientId;
    @ApiParam(value = "A space-delimited list of permissions that the application requires.", required = true)
    @QueryParam("scope")
    private String scope;
    @ApiParam(value = "The primary reason for using the state parameter is to mitigate CSRF attacks. An opaque value, used for security purposes. If this request parameter is set in the request, then it is returned to the application as part of the redirect_uri.", required = true)
    @QueryParam("state")
    private String state;
    @ApiParam(value = "Holds a URL. A successful response from this endpoint results in a redirect to this URL.", required = true)
    @QueryParam("redirect_uri")
    private String redirectUri;
    @ApiParam(value = "Informs the Authorization Server of the desired authorization processing flow", required = true, allowableValues = "code,token")
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