package webserver.handler;

import webserver.http.Request;
import webserver.http.Response;
import webserver.mvc.Handler;
import webserver.mvc.RedirectView;
import webserver.mvc.StaticResourceView;
import webserver.service.AuthUtil;
import webserver.mvc.ModelAndView;

public class MyPageHandler implements Handler {
    @Override
    public ModelAndView handle(Request request, Response response) {
        if(!AuthUtil.isAuthenticatedUser(request)){
            return new RedirectView("/login");
        }
        return new StaticResourceView("/mypage/index.html");
    }
}
