package webserver;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handler.*;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static Map<String, Handler> handlerMap;
    private static Handler staticResourceHandler;
    private Socket connection;

    static{
        handlerMap = new HashMap<>();
        handlerMap.put("/", new MainHandler());
        handlerMap.put("/registration", new RegisterFormHandler());
        handlerMap.put("/create", new CreateUserHandler());
        staticResourceHandler = new StaticResourceHandler();
    }

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.

            RequestGenerator requestGenerator = new RequestGenerator(in);
            Request request = requestGenerator.generate();

            logger.debug("request parsing complete");

            Handler handler = resovleHandler(request.getPath());
            Response response = handler.handle(request);

            ResponseWriter responseWriter = new ResponseWriter(out);
            responseWriter.write(response);

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
