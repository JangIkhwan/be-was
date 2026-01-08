package webserver.handler;

import webserver.service.AuthUtil;

public class MyPageHandler implements Handler{
    @Override
    public Response handle(Request request) {
        if(!AuthUtil.isAuthenticatedUser(request)){
            return Response.redirect("/login");
        }
        return Response.forward("/mypage/index.html");
    }
}
