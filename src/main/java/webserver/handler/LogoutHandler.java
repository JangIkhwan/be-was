package webserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.Request;
import webserver.http.Response;
import webserver.mvc.Handler;
import webserver.view.RedirectView;
import webserver.util.AuthUtil;
import webserver.session.SessionStore;
import webserver.mvc.ModelAndView;

public class LogoutHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(LogoutHandler.class);

    @Override
    public ModelAndView handle(Request request, Response response) {
        if(AuthUtil.isAuthenticatedUser(request)){
            String sid = request.getCookie("sid");
            SessionStore sessionStore = request.getSessionStore();
            sessionStore.remove(sid);
            logger.debug("session removed");
        }

        return new RedirectView("/");
    }
}
