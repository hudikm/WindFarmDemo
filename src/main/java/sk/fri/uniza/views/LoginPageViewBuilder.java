package sk.fri.uniza.views;

public class LoginPageViewBuilder {
    private String rootPath;
    private String sessionId;
    private String action;

    public LoginPageViewBuilder setRootPath(String rootPath) {
        this.rootPath = rootPath;
        return this;
    }

    public LoginPageViewBuilder setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public LoginPageViewBuilder setAction(String action) {
        this.action = action;
        return this;
    }

    public LoginPageView createLoginPageView() {
        return new LoginPageView(rootPath, sessionId, action);
    }
}