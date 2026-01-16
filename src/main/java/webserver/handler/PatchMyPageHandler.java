package webserver.handler;

import db.UserRepository;
import model.User;
import webserver.exception.BusinessException;
import webserver.http.Request;
import webserver.http.Response;
import webserver.mvc.Handler;
import webserver.mvc.ModelAndView;
import webserver.mvc.MultipartFile;
import webserver.util.AuthUtil;
import webserver.util.MultipartFileUtil;
import webserver.view.RedirectView;

import java.io.IOException;

public class PatchMyPageHandler implements Handler {
    private final UserRepository userRepository;

    public PatchMyPageHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ModelAndView handle(Request request, Response response) {
        if (!AuthUtil.isAuthenticatedUser(request)) {
            return new RedirectView("/login");
        }

        User cachedUser = AuthUtil.getAuthenticatedUser(request);
        User userByEmail = userRepository.findByEmail(cachedUser.getEmail())
                .orElseThrow(() -> new BusinessException());

        boolean changed = false;
        if(request.getMultipartFiles().size() == 1){
            MultipartFile multipartFile = request.getMultipartFiles().get(0);

            String imageUrl = null;
            try {
                imageUrl = MultipartFileUtil.saveFile("uploads/images", multipartFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            userByEmail.changeProfileImage(imageUrl);
            changed = true;
        }

        String name = request.getParameter("name");
        if(name != null && name.length() >= 4 && !name.isBlank()){
            userByEmail.changeName(name.trim());
            changed = true;
        }

        String password = request.getParameter("password");
        if(password != null && password.length() >= 4 && !password.isBlank()){
            userByEmail.changePassword(password.trim());
            changed = true;
        }

        if(changed){
            userRepository.update(userByEmail);
        }

        return new RedirectView("/mypage");
    }
}
