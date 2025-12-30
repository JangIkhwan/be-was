package webserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

public class CreateUserHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(CreateUserHandler.class);

    @Override
    public Response handle(Request request) {
        logger.debug("userId= {}", request.getParameter("userId"));
        return null;
    }
}
