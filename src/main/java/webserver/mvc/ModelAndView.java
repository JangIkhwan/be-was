package webserver.mvc;

import webserver.http.Response;

public interface ModelAndView {
    String getViewName();
    void render(Response response);
}
