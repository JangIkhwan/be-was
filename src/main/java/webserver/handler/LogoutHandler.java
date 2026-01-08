package webserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.service.AuthUtil;
import webserver.session.SessionStore;

public class LogoutHandler implements Handler{
    private static final Logger logger = LoggerFactory.getLogger(LogoutHandler.class);

    @Override
    public Response handle(Request request) {
        if(AuthUtil.isAuthenticatedUser(request)){
            String sid = request.getCookie("sid");
            SessionStore sessionStore = request.getSessionStore();
            sessionStore.remove(sid);
            logger.debug("session removed");
        }
        return Response.redirect("/");
    }
}
