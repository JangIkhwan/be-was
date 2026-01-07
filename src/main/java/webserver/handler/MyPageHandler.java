package webserver.handler;

import webserver.service.AuthService;

public class MyPageHandler implements Handler{
    private final AuthService authService;

    public MyPageHandler(AuthService authService){
        this.authService = authService;
    }

    @Override
    public Response handle(Request request) {
        if(!authService.isAuthenticatedUser(request)){
            return Response.redirect("/login");
        }
        return Response.forward("/mypage/index.html");
    }
}
