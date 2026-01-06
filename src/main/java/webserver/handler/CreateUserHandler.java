package webserver.handler;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.BusinessException;

public class CreateUserHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(CreateUserHandler.class);

    @Override
    public Response handle(Request request) {
        String userId = request.getParameter("email");
        String password = request.getParameter("password");
        String name = request.getParameter("nickname");
        String email = request.getParameter("email");

        logger.debug("userId = {} password = {} name = {} email = {}", userId, password, name, email);

        if(Database.findUserById(userId) != null){
            throw new BusinessException();
        }
        User user = new User(userId, password, name, email);
        Database.addUser(user);

        logger.debug("create user success");

        return Response.redirect("/index.html");
    }
}
