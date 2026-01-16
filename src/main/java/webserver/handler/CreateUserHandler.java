package webserver.handler;

import db.UserRepository;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.Request;
import webserver.http.Response;
import webserver.mvc.Handler;
import webserver.mvc.ModelAndView;
import webserver.view.FormErrorDynamicView;
import webserver.view.RedirectView;

import java.util.Map;

public class CreateUserHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(CreateUserHandler.class);
    private final UserRepository userRepository;

    public CreateUserHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ModelAndView handle(Request request, Response response) {
        String password = request.getParameter("password");
        String name = request.getParameter("nickname");
        String email = request.getParameter("email");

        logger.debug("password = {} name = {} email = {}", password, name, email);

        if(userRepository.findByEmail(email).isPresent()){
            return new FormErrorDynamicView("/registration/error.html", Map.of("error", "중복된 이메일이 존재합니다"));
        }

        if(password == null || name == null || email == null){
            return new FormErrorDynamicView("/registration/error.html", Map.of("error", "비밀번호, 닉네임, 이메일은 4자리 이상이어야 합니다"));
        }

        if(password.length() < 4 || name.length() < 4 || email.length() < 4 ){
            return new FormErrorDynamicView("/registration/error.html", Map.of("error", "비밀번호, 닉네임, 이메일은 4자리 이상이어야 합니다"));
        }

        User user = new User(password.trim(), name.trim(), email.trim());

        userRepository.save(user);

        logger.debug("create user success");

        return new RedirectView("/");
    }
}
