package webserver.handler;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.Request;
import webserver.http.Response;
import webserver.mvc.Handler;
import webserver.mvc.ModelAndDynamicView;
import webserver.mvc.StaticResourceView;
import webserver.util.AuthUtil;
import webserver.mvc.ModelAndView;

import java.util.HashMap;
import java.util.Map;

public class MainHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(MainHandler.class);

    public ModelAndView handle(Request request, Response response) {
        User loginUser = AuthUtil.getAuthenticatedUser(request);
        if(loginUser == null){
            return new StaticResourceView("/index.html");
        }

        logger.debug("session found");

        Map<String, String> model = new HashMap<>();
        model.put("name", loginUser.getName());
        return new ModelAndDynamicView(model,"/index_logined.html");

    }
}
