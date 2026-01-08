package webserver.handler;

import webserver.http.ModelAndViewImpl;
import webserver.service.AuthUtil;
import webserver.http.ModelAndView;

public class MyPageHandler implements Handler{
    @Override
    public ModelAndView handle(Request request, Response response) {
        if(!AuthUtil.isAuthenticatedUser(request)){
            return ModelAndViewImpl.redirect("/login");
        }
        return ModelAndViewImpl.forward("/mypage/index.html");
    }
}
