package webserver.handler;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.session.SessionStore;

import java.util.Map;

public class MainHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(MainHandler.class);

    public Response handle(Request request) {
        logger.debug("request = {}", request);

        String sid = request.getCookie("sid");
        if(!foundCookie(sid)) {
            return Response.forward("/index.html");
        }

        SessionStore sessionStore = request.getSessionStore();
        User loginedUser = (User) sessionStore.getSession(sid);
        if(!hasLogined(loginedUser)){
            return Response.forward("/index.html");
        }

        logger.debug("session found");

        Map<String, String> model = Map.of("name", loginedUser.getName());
        return Response.forwardDynamicHtml(model, "/index_logined.html");
    }

    private boolean foundCookie(String sid) {
        return sid != null;
    }

    private boolean hasLogined(User loginedUser) {
        return loginedUser != null;
    }
}
