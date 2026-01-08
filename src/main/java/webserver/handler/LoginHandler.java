package webserver.handler;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.Request;
import webserver.http.Response;
import webserver.mvc.Handler;
import webserver.mvc.RedirectView;
import webserver.mvc.StaticResourceView;
import webserver.session.SessionStore;
import webserver.mvc.ModelAndView;

import java.util.UUID;

public class LoginHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(LoginHandler.class);

    @Override
    public ModelAndView handle(Request request, Response response) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User userById = Database.findUserById(email);
        if(!foundUser(userById)){
            return new StaticResourceView("/login/error.html");
        }

        if(!matchedPassword(userById, password)){
            return new StaticResourceView("/login/error.html");
        }

        SessionStore sessionStore = request.getSessionStore();
        String sessionId = UUID.randomUUID().toString();
        sessionStore.addSession(sessionId, userById);

        logger.debug("session created = {}", sessionId);

        response.setCookie("sid", sessionId);
        response.setCookie("Path", "/");

        return new RedirectView("/");
    }

    private static boolean matchedPassword(User userById, String password) {
        return userById.getPassword().equals(password);
    }

    private boolean foundUser(User userById) {
        return userById != null;
    }
}
