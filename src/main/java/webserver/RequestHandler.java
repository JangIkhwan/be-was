package webserver;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.RequestParsingException;
import webserver.handler.*;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static Map<String, Handler> handlerMap;
    private static Handler staticResourceHandler;
    private Socket connection;

    static{
        handlerMap = new HashMap<>();
        handlerMap.put("GET /", new MainHandler());
        handlerMap.put("GET /registration", new RegisterFormHandler());
        handlerMap.put("POST /create", new CreateUserHandler());
        handlerMap.put("GET /login", new LoginFormHandler());
        staticResourceHandler = new StaticResourceHandler();
    }

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            ResponseWriter responseWriter = new ResponseWriter(out);
            try{
                RequestGenerator requestGenerator = new RequestGenerator(in);
                Request request = requestGenerator.generate();

                logger.debug("request parsing complete");

                Handler handler = resovleHandler(request.getHandlerKey());
                Response response = handler.handle(request);
                responseWriter.write(response);
            }
            catch (RequestParsingException e){
                responseWriter.write(Response.badRequest());
            }
            catch (RuntimeException e){
                responseWriter.write(Response.internalServerError());
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
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
