package webserver.http;

import webserver.handler.Response;

import java.util.Set;

public interface ModelAndView {
    ModelAndView addModelAttribute(String name, String value);
    String getViewName();
    Set<String> getModelNames();
    String getModelAttribute(String name);
    void render(Response response);
}
