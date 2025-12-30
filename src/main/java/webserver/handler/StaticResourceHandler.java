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
        String contentType = resolveContentType(path);
        try {
            byte[] body = Files.readAllBytes(new File("./src/main/resources/static" + path).toPath());
            return Response.ok(body, contentType);
        } catch (IOException e) {
            logger.error("error occurred while reading static resource");
        }
        return Response.notFound();
    }

    private String resolveContentType(String path) {
        if(path.endsWith(".js")) return "text/javascript";
        if(path.endsWith(".css")) return "text/css";
        if(path.endsWith(".svg")) return "image/svg+xml";
        if(path.endsWith(".ico")) return "image/vnd.microsoft.icon";
        if(path.endsWith(".png")) return "image/png";
        if(path.endsWith(".jpg")) return "image/jpg";
        return "text/html";
    }
}
