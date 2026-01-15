package webserver.handler;

import db.UserRepository;
import model.User;
import webserver.exception.BusinessException;
import webserver.http.Request;
import webserver.http.Response;
import webserver.mvc.*;
import webserver.mvc.ModelAndView;
import webserver.view.MyPageDynamicView;
import webserver.util.AuthUtil;
import webserver.view.RedirectView;

import java.util.HashMap;

public class MyPageHandler implements Handler {
    private final UserRepository userRepository;

    public MyPageHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ModelAndView handle(Request request, Response response) {
        if(!AuthUtil.isAuthenticatedUser(request)){
            return new RedirectView("/login");
        }

        User cachedUser = AuthUtil.getAuthenticatedUser(request);

        User user = userRepository.findByEmail(cachedUser.getEmail())
                .orElseThrow(() -> new BusinessException());

        HashMap<String, Object> model = new HashMap<>();
        model.put("name", user.getName());
        String imageUrl = user.getImageUrl();
        if(imageUrl != null){
            model.put("profile_image", imageUrl);
        }
        return new MyPageDynamicView(model, "/mypage/index.html");
    }
}
