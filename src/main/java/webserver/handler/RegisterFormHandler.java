package webserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.Request;
import webserver.http.Response;
import webserver.mvc.Handler;
import webserver.mvc.ModelAndView;
import webserver.view.StaticResourceView;

public class RegisterFormHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(RegisterFormHandler.class);

    @Override
    public ModelAndView handle(Request request, Response response) {
        return new StaticResourceView("/registration/index.html");
    }
}
