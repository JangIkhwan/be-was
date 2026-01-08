package webserver.mvc;

import webserver.handler.Request;
import webserver.handler.Response;

public interface Handler {
    ModelAndView handle(Request request, Response response);
}
