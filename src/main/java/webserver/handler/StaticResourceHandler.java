package webserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.mvc.Handler;
import webserver.mvc.StaticResourceView;
import webserver.mvc.ModelAndView;

public class StaticResourceHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(StaticResourceHandler.class);

    @Override
    public ModelAndView handle(Request request, Response response) {
        return new StaticResourceView(request.getPath());
    }
}
