package webserver.http;

import java.util.Set;

public interface ModelAndView {
    ModelAndView addModelAttribute(String name, String value);
    Boolean isRedirect();
    Boolean isDynamic();
    String getViewName();
    Set<String> getModelNames();
    String getModelAttribute(String name);
}
