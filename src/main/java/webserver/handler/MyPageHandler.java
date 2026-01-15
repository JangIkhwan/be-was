package webserver.handler;

import model.User;
import webserver.http.Request;
import webserver.http.Response;
import webserver.mvc.*;
import webserver.view.MyPageDynamicView;
import webserver.util.AuthUtil;

import java.util.HashMap;

public class MyPageHandler implements Handler {
    @Override
    public ModelAndView handle(Request request, Response response) {
        if(!AuthUtil.isAuthenticatedUser(request)){
            return new RedirectView("/login");
        }

        User user = AuthUtil.getAuthenticatedUser(request);

        HashMap<String, Object> model = new HashMap<>();
        model.put("name", user.getName());
        return new MyPageDynamicView(model, "/mypage/index.html");
    }
}
