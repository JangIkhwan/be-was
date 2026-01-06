package webserver.handler;

import webserver.session.SessionStore;

import java.util.Map;

public class Request {
    private String method;
    private String path;
    private Map<String, String> params;
    private SessionStore sessionStore;

    public Request(String method, String path, Map<String, String> params, SessionStore sessionStore) {
        this.method = method;
        this.path = path;
        this.params = params;
        this.sessionStore = sessionStore;
    }

    public String getPath() {
        return path;
    }

    public String getParameter(String key){
        return params.get(key);
    }

    public String getHandlerKey() {
        return method + " " + path;
    }

    public SessionStore getSessionStore(){
        return sessionStore;
    }
}
