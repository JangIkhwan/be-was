package webserver.handler;

import db.Database;
import db.UserRepository;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.BusinessException;
import webserver.http.Request;
import webserver.http.Response;
import webserver.mvc.Handler;
import webserver.mvc.ModelAndView;
import webserver.mvc.StaticResourceView;

public class CreateUserHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(CreateUserHandler.class);

    @Override
    public ModelAndView handle(Request request, Response response) {
        String userId = request.getParameter("email");
        String password = request.getParameter("password");
        String name = request.getParameter("nickname");
        String email = request.getParameter("email");

        logger.debug("userId = {} password = {} name = {} email = {}", userId, password, name, email);

        if(Database.findUserById(userId) != null){
            throw new BusinessException();
        }

        UserRepository userRepository = new UserRepository();
        userRepository.save(null);
        User user = new User(userId, password, name, email);
        Database.addUser(user);

        logger.debug("create user success");

        return new StaticResourceView("/index.html");
    }
}
