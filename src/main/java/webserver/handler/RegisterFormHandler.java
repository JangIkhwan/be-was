package webserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class RegisterFormHandler implements Handler{
    private static final Logger logger = LoggerFactory.getLogger(RegisterFormHandler.class);

    @Override
    public Response handle(Request request) {
        try {
            byte[]  body = Files.readAllBytes(Path.of("./src/main/resources/static/registration/index.html"));
            return Response.ok(body, "text/html");
        }
        catch (IOException e) {
            logger.error("error occurred while reading static resource");
        }
        return Response.notFound();

    }
}
