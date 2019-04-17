package sk.fri.uniza.views;

import io.dropwizard.views.View;

public class LoginPageView extends View {

    private String rootPath;
    private String sessionId;
    private String action;

    LoginPageView(String rootPath, String sessionId, String action) {
        super("loginPage.ftl");
        this.rootPath = rootPath;
        this.sessionId = sessionId;
        this.action = action;
    }


    public String getSessionId() {
        return sessionId;
    }


    public String getAction() {
        return action;
    }

    public String getRootPath() {
        return rootPath;
    }

}
