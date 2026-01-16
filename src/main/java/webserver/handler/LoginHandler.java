package webserver.handler;

import db.UserRepository;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.Request;
import webserver.http.Response;
import webserver.mvc.Handler;
import webserver.view.RedirectView;
import webserver.view.StaticResourceView;
import webserver.session.SessionStore;
import webserver.mvc.ModelAndView;

import java.util.Optional;
import java.util.UUID;

public class LoginHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(LoginHandler.class);
    private final UserRepository userRepository;

    public LoginHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ModelAndView handle(Request request, Response response) {
        String email = request.getParameter("email").trim();
        String password = request.getParameter("password").trim();

        Optional<User> byEmail = userRepository.findByEmail(email);
        if(byEmail.isEmpty()){
            logger.debug("유저 없음");
            return new StaticResourceView("/login/error.html");
        }

        User user = byEmail.get();
        if(!matchedPassword(user.getPassword(), password)){
            logger.debug("비번 일치 안함");
            return new StaticResourceView("/login/error.html");
        }

        SessionStore sessionStore = request.getSessionStore();
        String sessionId = UUID.randomUUID().toString();
        sessionStore.addSession(sessionId, user);

        logger.debug("session created = {}", sessionId);

        response.setCookie("sid", sessionId);
        response.setCookie("Path", "/");

        return new RedirectView("/");
    }

    private static boolean matchedPassword(String originalPassword, String password) {
        return originalPassword.equals(password);
    }
}
