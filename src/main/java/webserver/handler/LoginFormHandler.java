package webserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.ModelAndView;
import webserver.http.StaticResourceView;

public class LoginFormHandler implements Handler{
    private static final Logger logger = LoggerFactory.getLogger(LoginFormHandler.class);

    @Override
    public ModelAndView handle(Request request, Response response) {
        return new StaticResourceView("/login/index.html");
    }
}
