package webserver.handler;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.ModelAndViewImpl;
import webserver.service.AuthUtil;
import webserver.http.ModelAndView;

public class MainHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(MainHandler.class);

    public ModelAndView handle(Request request, Response response) {
        User loginUser = AuthUtil.getAuthenticatedUser(request);
        if(loginUser == null){
            return ModelAndViewImpl.forward("/index.html");
        }

        logger.debug("session found");

        return ModelAndViewImpl.forwardDynamic("/index_logined.html")
                .addModelAttribute("name", loginUser.getName());
    }
}
