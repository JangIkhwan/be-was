package webserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.mvc.Handler;
import webserver.mvc.ModelAndView;
import webserver.mvc.StaticResourceView;

public class LoginFormHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(LoginFormHandler.class);

    @Override
    public ModelAndView handle(Request request, Response response) {
        return new StaticResourceView("/login/index.html");
    }
}
