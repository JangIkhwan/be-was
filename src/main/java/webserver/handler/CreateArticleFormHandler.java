package webserver.handler;

import webserver.http.Request;
import webserver.http.Response;
import webserver.mvc.Handler;
import webserver.mvc.ModelAndView;
import webserver.mvc.RedirectView;
import webserver.mvc.StaticResourceView;
import webserver.util.AuthUtil;

public class CreateArticleFormHandler implements Handler {
    @Override
    public ModelAndView handle(Request request, Response response) {
        if(!AuthUtil.isAuthenticatedUser(request)){
            return new RedirectView("/login");
        }

        return new StaticResourceView("/article/index.html");
    }
}
