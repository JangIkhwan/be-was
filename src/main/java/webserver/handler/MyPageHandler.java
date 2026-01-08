package webserver.handler;

import webserver.http.RedirectView;
import webserver.http.StaticResourceView;
import webserver.service.AuthUtil;
import webserver.http.ModelAndView;

public class MyPageHandler implements Handler{
    @Override
    public ModelAndView handle(Request request, Response response) {
        if(!AuthUtil.isAuthenticatedUser(request)){
            return new RedirectView("/login");
        }
        return new StaticResourceView("/mypage/index.html");
    }
}
