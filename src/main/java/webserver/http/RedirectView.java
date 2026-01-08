package webserver.http;

import webserver.handler.Response;

import java.util.Set;

public class RedirectView implements ModelAndView {
    private String viewName;

    public RedirectView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public ModelAndView addModelAttribute(String name, String value) {
        return this;
    }

    @Override
    public String getViewName() {
        return viewName;
    }

    @Override
    public Set<String> getModelNames() {
        return Set.of();
    }

    @Override
    public String getModelAttribute(String name) {
        return "";
    }

    @Override
    public void render(Response response) {
        response.setRedirect(viewName);
    }
}
