package webserver.http;

import webserver.mvc.MultipartFile;
import webserver.session.SessionStore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static webserver.constant.HttpHeader.COOKIE;

public class Request {
    private static final String COOKIE_DELIMITER = ";";
    private static final String COOKIE_KEY_AND_VALUE_DELIMITER = "=";
    private String method;
    private String path;
    private Map<String, String> headers;
    private Map<String, String> params;
    private Map<String, String> cookies;
    private List<MultipartFile> multipartFiles;
    private SessionStore sessionStore;

    public Request(String method, String path, Map<String, String> header, Map<String, String> params, List<MultipartFile> multipartFiles, SessionStore sessionStore) {
        this.method = method;
        this.path = path;
        this.headers = header;
        this.params = params;
        this.cookies = parseCookie(header);
        this.multipartFiles = multipartFiles;
        this.sessionStore = sessionStore;
    }

    private Map<String, String> parseCookie(Map<String, String> header) {
        if(!header.containsKey(COOKIE.getHeader())){
            return new HashMap<>();
        }
        Map<String, String> map = new HashMap<>();
        String cookieHeaderValue = header.get(COOKIE.getHeader());
        String[] keyAndValues = cookieHeaderValue.split(COOKIE_DELIMITER);
        for(String keyAndValue : keyAndValues){
            String[] split = keyAndValue.trim().split(COOKIE_KEY_AND_VALUE_DELIMITER);
            if(split.length != 2){
                continue;
            }
            String key = split[0].trim();
            String value = split[1].trim();
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

    public String getMethod() {
        return method;
    }

    public SessionStore getSessionStore(){
        return sessionStore;
    }

    public String getCookie(String key) {
        return cookies.get(key);
    }

    public List<MultipartFile> getMultipartFiles() {
        return multipartFiles;
    }

    public void setParameter(String name, String value) {
        params.put(name, value);
    }


    @Override
    public String toString() {
        return "Request{" +
                "method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", header=" + headers +
                ", params=" + params +
                ", sessionStore=" + sessionStore +
                '}';
    }
}
