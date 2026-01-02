package webserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterFormHandler implements Handler{
    private static final Logger logger = LoggerFactory.getLogger(RegisterFormHandler.class);

    @Override
    public Response handle(Request request) {
        return Response.redirect("/registration/index.html");
    }
}
