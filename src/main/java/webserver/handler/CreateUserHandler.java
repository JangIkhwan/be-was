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
import webserver.view.RedirectView;

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
            throw new BusinessException();
        }

        User user = new User(password, name, email);

        userRepository.save(user);

        logger.debug("create user success");

        return new RedirectView("/");
    }
}
