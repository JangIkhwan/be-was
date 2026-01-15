package webserver.mvc;

import webserver.http.Request;

public interface Routing {
    boolean supportsUri(String uri);

    boolean supportsMethod(String method);

    Handler resolveHandler(Request method);
}
