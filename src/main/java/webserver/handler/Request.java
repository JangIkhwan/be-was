package webserver.handler;

import webserver.session.SessionStore;

import java.util.HashMap;
import java.util.Map;

public class Request {
    private static final String COOKIE = "Cookie";
    private String method;
    private String path;
    private Map<String, String> header;
    private Map<String, String> params;
    private Map<String, String> cookies;
    private SessionStore sessionStore;

    public Request(String method, String path, Map<String, String> header, Map<String, String> params, SessionStore sessionStore) {
        this.method = method;
        this.path = path;
        this.header = header;
        this.params = params;
        this.cookies = parseCookie(header);
        this.sessionStore = sessionStore;
    }

    private Map<String, String> parseCookie(Map<String, String> header) {
        if(!header.containsKey(COOKIE)){
            return new HashMap<>();
        }
        Map<String, String> map = new HashMap<>();
        String cookieHeaderValue = header.get(COOKIE);
        String[] keyAndValues = cookieHeaderValue.split(";");
        for(String keyAndValue : keyAndValues){
            String[] split = keyAndValue.trim().split("=");
            if(split.length != 2){
                continue;
            }
            String key = split[0].trim();
            String value = split[0].trim();
            map.put(key, value);
        }
        return map;
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

    public String getHeader(String headerField) {
        return header.get(headerField);
    }

    public String getCookie(String key) {
        return cookies.get(key);
    }

    @Override
    public String toString() {
        return "Request{" +
                "method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", header=" + header +
                ", params=" + params +
                ", sessionStore=" + sessionStore +
                '}';
    }
}
