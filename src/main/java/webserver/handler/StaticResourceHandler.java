package webserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

public class StaticResourceHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    @Override
    public Response handle(Request request) {
        return Response.forward(request.getPath());
    }
}
