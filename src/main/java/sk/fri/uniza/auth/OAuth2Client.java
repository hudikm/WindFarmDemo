package sk.fri.uniza.auth;

import javax.ws.rs.FormParam;

public class OAuth2Client {


    private String clientId;
    private String redirectUri;

    public OAuth2Client(String clientId, String redirectUri, String clientSecrete) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.clientSecrete = clientSecrete;
    }

    private String clientSecrete;

    public String getClientSecrete() {
        return clientSecrete;
    }

    public void setClientSecrete(String clientSecrete) {
        this.clientSecrete = clientSecrete;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }


}
