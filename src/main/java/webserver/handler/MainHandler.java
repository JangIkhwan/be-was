package webserver.handler;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.ModelAndDynamicView;
import webserver.http.StaticResourceView;
import webserver.service.AuthUtil;
import webserver.http.ModelAndView;

public class MainHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(MainHandler.class);

    public ModelAndView handle(Request request, Response response) {
        User loginUser = AuthUtil.getAuthenticatedUser(request);
        if(loginUser == null){
            return new StaticResourceView("/index.html");
        }

        logger.debug("session found");

        return new ModelAndDynamicView("/index_logined.html")
                .addModelAttribute("name", loginUser.getName());
    }
}
