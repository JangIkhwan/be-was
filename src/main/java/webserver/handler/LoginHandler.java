package webserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(LoginHandler.class);

    @Override
    public Response handle(Request request) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        logger.debug("email = {} password = {}", email, password);

        return Response.redirect("/index.html");
    }
}
