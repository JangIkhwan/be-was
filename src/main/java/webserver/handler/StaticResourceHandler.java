package webserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;
import webserver.constant.FileMimeType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class StaticResourceHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    @Override
    public Response handle(Request request) {
        try {
            String path = request.getPath();
            byte[] body = Files.readAllBytes(new File("./src/main/resources/static" + path).toPath());
            String contentType = resolveContentType(path);
            return Response.ok(body, contentType);
        } catch (IOException e) {
            logger.error("error occurred while reading static resource");
        }
        return Response.notFound();
    }

    private String resolveContentType(String path) {
        return FileMimeType.resolveMimeType(path);
    }
}
