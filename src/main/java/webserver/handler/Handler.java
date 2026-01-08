package webserver.handler;

import webserver.http.ModelAndView;

public interface Handler {
    ModelAndView handle(Request request, Response response);
}
