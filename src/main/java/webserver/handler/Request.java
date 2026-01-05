package webserver.handler;

import java.util.Map;

public class Request {
    private String method;
    private String path;
    private Map<String, String> params;

    public Request(String method, String path, Map<String, String> params) {
        this.method = method;
        this.path = path;
        this.params = params;
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
}
