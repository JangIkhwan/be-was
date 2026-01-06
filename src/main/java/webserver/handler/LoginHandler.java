package webserver.handler;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.session.SessionStore;

import java.util.UUID;

public class LoginHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(LoginHandler.class);

    @Override
    public Response handle(Request request) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        logger.debug("email = {} password = {}", email, password);

        User userById = Database.findUserById(email);
        if(!foundUser(userById)){
            return Response.badRequest();
        }

        if(!matchedPassword(userById, password)){
            return Response.badRequest();
        }

        SessionStore sessionStore = request.getSessionStore();
        String sessionId = UUID.randomUUID().toString();
        sessionStore.addSession(sessionId, userById.getUserId());

        logger.debug("session created");

        Response response = Response.redirect("/index.html");
        response.setCookie("sid", sessionId);
        response.setCookie("Path", "/");

        return response;
    }

    private static boolean matchedPassword(User userById, String password) {
        return userById.getPassword().equals(password);
    }

    private boolean foundUser(User userById) {
        return userById != null;
    }
}
