package webserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class StaticResourceHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    @Override
    public Response handle(Request request) {
        String path = request.getPath();
        if(path.endsWith(".html")){
            try {
                byte[] body = Files.readAllBytes(new File("./src/main/resources/static" + path).toPath());
                return new Response(body, "text/html");
            } catch (IOException e) {
                logger.error("error occurred while reading static resource");
            }
        }
        if(path.endsWith(".css")){
            try {
                byte[] body = Files.readAllBytes(new File("./src/main/resources/static" + path).toPath());
                return new Response(body, "text/css");
            } catch (IOException e) {
                logger.error("error occurred while reading static resource");
            }
        }
        if(path.endsWith(".svg")){
            try {
                byte[] body = Files.readAllBytes(new File("./src/main/resources/static" + path).toPath());
                return new Response(body, "image/svg+xml");
            } catch (IOException e) {
                logger.error("error occurred while reading static resource");
            }
        }
        if(path.endsWith(".ico")){
            try {
                byte[] body = Files.readAllBytes(new File("./src/main/resources/static" + path).toPath());
                return new Response(body, "image/vnd.microsoft.icon");
            } catch (IOException e) {
                logger.error("error occurred while reading static resource");
            }
        }
        return null;
    }
}
