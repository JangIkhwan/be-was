package webserver.handler;

import java.util.Map;

public class Request {
    private String path;
    private Map<String, String> params;

    public Request(String path, Map<String, String> params) {
        this.path = path;
        this.params = params;
    }

    public String getPath() {
        return path;
    }

    public String getParameter(String key){
        return params.get(key);
    }
}
