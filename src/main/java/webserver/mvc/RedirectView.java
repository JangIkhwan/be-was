package webserver.mvc;

import webserver.http.Response;

public class RedirectView implements ModelAndView {
    private String viewName;

    public RedirectView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public String getViewName() {
        return viewName;
    }

    @Override
    public void render(Response response) {
        response.setRedirect(viewName);
    }
}
