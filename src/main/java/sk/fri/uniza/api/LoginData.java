package sk.fri.uniza.api;

import javax.ws.rs.FormParam;

public class LoginData {

    @FormParam("username")
    private String username;
    @FormParam("password")
    private String password;
    @FormParam("sessionId")
    private String sessionId;
    @FormParam("action")
    private Object action;
    @FormParam("stay_signin")
    private Boolean staySignin;

    @FormParam("stay_signin")
    public Boolean getStaySignin() {
        return staySignin;
    }

    @FormParam("stay_signin")
    public void setStaySignin(Boolean stay_signin) {
        this.staySignin = stay_signin;
    }

    @FormParam("username")
    public String getUsername() {
        return username;
    }

    @FormParam("username")
    public void setUsername(String username) {
        this.username = username;
    }

    @FormParam("password")
    public String getPassword() {
        return password;
    }

    @FormParam("password")
    public void setPassword(String password) {
        this.password = password;
    }

    @FormParam("sessionId")
    public String getSessionId() {
        return sessionId;
    }

    @FormParam("sessionId")
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @FormParam("action")
    public Object getAction() {
        return action;
    }

    @FormParam("action")
    public void setAction(Object action) {
        this.action = action;
    }

}