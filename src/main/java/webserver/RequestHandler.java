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

    private Map<String, Handler> handlerMap;

    private Handler staticResourceHandler;

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.handlerMap = new HashMap<>();
        this.handlerMap.put("/", new MainHandler());
        this.handlerMap.put("/registration", new RegisterFormHandler());
        staticResourceHandler = new StaticResourceHandler();
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.

            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String line = br.readLine();
            logger.debug("http first line {}", line);
            String path = line.split(" ")[1];

            logger.debug("path {}", path);

            ResponseWriter responseWriter = new ResponseWriter(out);
            Handler handler = resovleHandler(path);
            Response response = handler.handle(new Request(path));
            sendResponse(response, responseWriter);

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

    private static void sendResponse(Response response, ResponseWriter responseWriter) {
        if(response == null){
            responseWriter.write404Response();
        }
        responseWriter.write200Response(response.getBody(), response.getContentType());
    }
}
