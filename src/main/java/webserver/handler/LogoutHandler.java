package webserver.handler;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.session.SessionStore;

public class LogoutHandler implements Handler{
    private static final Logger logger = LoggerFactory.getLogger(LogoutHandler.class);

    @Override
    public Response handle(Request request) {
        String sid = request.getCookie("sid");
        if(sid == null){
            return Response.redirect("/");
        }
        SessionStore sessionStore = request.getSessionStore();
        User loginUser = (User) sessionStore.getSession(sid);
        if(loginUser == null){
            return Response.redirect("/");
        }

        logger.debug("session removed");

        sessionStore.remove(sid);
        return Response.redirect("/");
    }
}
