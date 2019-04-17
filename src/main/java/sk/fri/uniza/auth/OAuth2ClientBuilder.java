package sk.fri.uniza.auth;

public class OAuth2ClientBuilder {
    private String clientId;
    private String redirectUri;
    private String clientSecrete;

    public OAuth2ClientBuilder setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public OAuth2ClientBuilder setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
        return this;
    }

    public OAuth2ClientBuilder setClientSecrete(String clientSecrete) {
        this.clientSecrete = clientSecrete;
        return this;
    }

    public OAuth2Client createOAuth2Client() {
        return new OAuth2Client(clientId, redirectUri, clientSecrete);
    }
}