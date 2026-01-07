package webserver.http;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.RequestParsingException;
import webserver.handler.*;
import webserver.service.AuthService;
import webserver.session.SessionStore;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static Map<String, Handler> handlerMap;
    private static Handler staticResourceHandler;
    private static SessionStore sessionStore;
    private Socket connection;

    static{
        AuthService authService = new AuthService();
        handlerMap = new HashMap<>();
        handlerMap.put("GET /", new MainHandler(authService));
        handlerMap.put("GET /registration", new RegisterFormHandler());
        handlerMap.put("POST /create", new CreateUserHandler());
        handlerMap.put("GET /login", new LoginFormHandler());
        handlerMap.put("POST /login", new LoginHandler());
        handlerMap.put("POST /logout", new LogoutHandler());
        handlerMap.put("GET /mypage", new MyPageHandler(authService));
        staticResourceHandler = new StaticResourceHandler();
        sessionStore = new SessionStore();
    }

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            processRequest(out, in);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void processRequest(OutputStream out, InputStream in) throws IOException {
        ResponseWriter responseWriter = new ResponseWriter(out);
        try{
            RequestGenerator requestGenerator = new RequestGenerator(in);
            Request request = requestGenerator.generate(sessionStore);

            logger.debug("request parsing complete");
            logger.debug("request={}", request);

            Handler handler = resovleHandler(request.getHandlerKey());
            Response response = handler.handle(request);

            logger.debug("response={}", response);

            responseWriter.write(response);
        }
        catch (RequestParsingException e){
            responseWriter.write(Response.badRequest());
        }
        catch (RuntimeException e){
            responseWriter.write(Response.internalServerError());
        }
    }

    private Handler resovleHandler(String path) {
        Handler handler = handlerMap.get(path);
        if(handler == null){
            handler = staticResourceHandler;
        }
        return handler;
    }
}
