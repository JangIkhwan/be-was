package webserver.mvc;

import webserver.http.Request;

import java.util.Map;

public class SimpleRouting implements Routing {
    private String uri;
    private Map<String, Handler> handlerMap;

    public SimpleRouting(String uri, Map<String, Handler> handlerMap) {
        this.uri = uri;
        this.handlerMap = handlerMap;
    }

    @Override
    public boolean supportsUri(String uri) {
        return this.uri.equals(uri);
    }

    @Override
    public boolean supportsMethod(String method) {
        return this.handlerMap.containsKey(method);
    }

    @Override
    public Handler resolveHandler(Request request) {
        return handlerMap.get(request.getMethod());
    }
}
