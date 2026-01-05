package webserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginFormHandler implements Handler{
    private static final Logger logger = LoggerFactory.getLogger(LoginFormHandler.class);

    @Override
    public Response handle(Request request) {
        return Response.forward("/login/index.html");
    }
}
