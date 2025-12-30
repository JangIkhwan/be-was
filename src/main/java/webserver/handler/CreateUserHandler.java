package webserver.handler;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateUserHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(CreateUserHandler.class);

    @Override
    public Response handle(Request request) {
        logger.debug("userId= {}", request.getParameter("userId"));

        User user = new User(request.getParameter("user"), request.getParameter("password"), request.getParameter("name"), request.getParameter("email"));
        Database.addUser(user);

        logger.debug("create user success");

        Response response = new Response()
                .setRedirectUrl("/index.html");
        return response;
    }
}
