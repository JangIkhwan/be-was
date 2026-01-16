package webserver.handler;

import db.UserRepository;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.BusinessException;
import webserver.http.Request;
import webserver.http.Response;
import webserver.mvc.Handler;
import webserver.mvc.ModelAndView;
import webserver.util.AuthUtil;
import webserver.view.RedirectView;

public class DeleteProfileImageHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(DeleteProfileImageHandler.class);
    private final UserRepository userRepository;

    public DeleteProfileImageHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ModelAndView handle(Request request, Response response) {
        if (!AuthUtil.isAuthenticatedUser(request)) {
            return new RedirectView("/login");
        }

        User cached = AuthUtil.getAuthenticatedUser(request);

        User user = userRepository.findByEmail(cached.getEmail())
                .orElseThrow(() -> new BusinessException());

        user.changeProfileImage(null);

        userRepository.update(user);

        logger.debug("프로필 이미지 삭제");

        return new RedirectView("/mypage");
    }
}
