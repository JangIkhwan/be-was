package webserver.mvc;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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
    private static List<Routing> routingTable;
    private static Handler staticResourceHandler;
    private static SessionStore sessionStore;
    private Socket connection;

    static{
        UserRepository userRepository = new UserRepositoryImpl();
        ArticleRepository articleRepository = new ArticleRepositoryImpl();

        routingTable = new ArrayList<>();
        routingTable.add(new SimpleRouting("/", Map.of("GET", new MainHandler(articleRepository, userRepository))));
        routingTable.add(new SimpleRouting("/registration", Map.of("GET", new RegisterFormHandler())));
        routingTable.add(new SimpleRouting("/create", Map.of("POST", new CreateUserHandler(userRepository))));
        routingTable.add(new SimpleRouting("/login", Map.of("GET", new LoginFormHandler(), "POST", new LoginHandler(userRepository))));
        routingTable.add(new SimpleRouting("/logout", Map.of("POST", new LogoutHandler())));
        routingTable.add(new SimpleRouting("/mypage", Map.of("GET", new MyPageHandler())));
        routingTable.add(new SimpleRouting("/article/create-form", Map.of("GET", new CreateArticleFormHandler())));
        routingTable.add(new SimpleRouting("/article", Map.of("POST", new CreateArticleHandler(articleRepository))));
        routingTable.add(new PathVariableRouting("/uploads/images/{imageUrl}", Map.of("GET", new GetImageHandler())));

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

            Handler handler = resovleHandler(request);
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

    private Handler resovleHandler(Request request) {
        for(Routing routing : routingTable){
            if(routing.supportsUri(request.getPath())){
                if(!routing.supportsMethod(request.getMethod())){
                    throw new MethodNotAllowedException();
                }
                return routing.resolveHandler(request);
            }
        }
        return staticResourceHandler;
    }
}
