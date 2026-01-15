package webserver.mvc;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import db.ArticleRepository;
import db.ArticleRepositoryImpl;
import db.UserRepository;
import db.UserRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.BadRequestException;
import webserver.exception.MethodNotAllowedException;
import webserver.exception.StaticResourceNotFoundException;
import webserver.handler.*;
import webserver.http.Request;
import webserver.http.Response;
import webserver.session.SessionStore;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static Map<String, Map<String, Handler>> routingTable;
    private static Handler staticResourceHandler;
    private static SessionStore sessionStore;
    private Socket connection;

    static{
        UserRepository userRepository = new UserRepositoryImpl();
        ArticleRepository articleRepository = new ArticleRepositoryImpl();
        routingTable = new HashMap<>();
        routingTable.put("/", Map.of("GET", new MainHandler(articleRepository, userRepository)));
        routingTable.put("/registration", Map.of("GET", new RegisterFormHandler()));
        routingTable.put("/create", Map.of("POST", new CreateUserHandler(userRepository)));
        routingTable.put("/login", Map.of("GET", new LoginFormHandler(), "POST", new LoginHandler(userRepository)));
        routingTable.put("/logout", Map.of("POST", new LogoutHandler()));
        routingTable.put("/mypage", Map.of("GET", new MyPageHandler()));
        routingTable.put("/article/create-form", Map.of("GET", new CreateArticleFormHandler()));
        routingTable.put("/article", Map.of("POST", new CreateArticleHandler(articleRepository)));
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
            RequestParser requestParser = new RequestParser(in);

            logger.debug("multiparts size : {}", requestParser.getMultipartFiles().size());

            Request request = new Request(requestParser.getMethod(), requestParser.getPath(), requestParser.getHeaders(), requestParser.getParams(), requestParser.getMultipartFiles(), sessionStore);
            Response response = new Response();

            logger.debug("request parsing complete");
            logger.debug("request={}", request);

            Handler handler = resovleHandler(request.getPath(), request.getMethod());
            ModelAndView mav = handler.handle(request, response);

            mav.render(response);

            logger.debug("response={}", response);

            responseWriter.write(response);
        }
        catch(StaticResourceNotFoundException e){
            logger.debug("static resouce not found error", e);
            sendErrorPage(responseWriter, Response.notFound(), new StaticResourceView("/error/404_error.html"));
        }
        catch (MethodNotAllowedException e){
            logger.debug("method not allowed error", e);
            sendErrorPage(responseWriter, Response.methodNotAllowed(), new StaticResourceView("/error/405_error.html"));
        }
        catch (BadRequestException e){
            logger.debug("request parsing error", e);
            sendErrorPage(responseWriter, Response.badRequest(), new StaticResourceView("/error/400_error.html"));

        }
        catch (RuntimeException e){
            logger.debug("internal server error", e);
            sendErrorPage(responseWriter, Response.internalServerError(), new StaticResourceView("/error/500_error.html"));
        }
    }

    private static void sendErrorPage(ResponseWriter responseWriter, Response response, ModelAndView view) {
        view.render(response);
        responseWriter.write(response);
    }

    private Handler resovleHandler(String uri, String method) {
        Map<String, Handler> methodHandlers = routingTable.get(uri);
        if(methodHandlers != null){
            Handler handler = methodHandlers.get(method);
            if(handler == null){
                throw new MethodNotAllowedException();
            }
            return handler;
        }
        return staticResourceHandler;
    }
}
