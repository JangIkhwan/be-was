package webserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.ModelAndView;
import webserver.http.StaticResourceView;

public class RegisterFormHandler implements Handler{
    private static final Logger logger = LoggerFactory.getLogger(RegisterFormHandler.class);

    @Override
    public ModelAndView handle(Request request, Response response) {
        return new StaticResourceView("/registration/index.html");
    }
}
