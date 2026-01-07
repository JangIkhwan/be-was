package webserver.handler;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.service.AuthService;

import java.util.Map;

public class MainHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(MainHandler.class);
    private final AuthService authService;

    public MainHandler(AuthService authService){
        this.authService = authService;
    }

    public Response handle(Request request) {
        User loginUser = authService.getAuthenticatedUser(request);
        if(loginUser == null){
            return Response.forward("/index.html");
        }

        logger.debug("session found");

        Map<String, String> model = Map.of("name", loginUser.getName());
        return Response.forwardDynamicHtml(model, "/index_logined.html");
    }
}
