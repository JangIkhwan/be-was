package webserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.ModelAndViewImpl;
import webserver.http.RequestHandler;
import webserver.http.ModelAndView;

public class StaticResourceHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    @Override
    public ModelAndView handle(Request request, Response response) {
        return ModelAndViewImpl.forward(request.getPath());
    }
}
