package webserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.constant.FileMimeType;
import webserver.handler.Response;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ViewRenderer {
    private static final Logger logger = LoggerFactory.getLogger(ViewRenderer.class);

    public ViewRenderer() { }

    public void render(ModelAndView mav, Response response) {
        if(mav.isRedirect()){
            response.redirect(mav.getViewName());
            return;
        }
        if(mav.isDynamic()){
            renderDynamicView(mav, response);
            return;
        }
        renderStaticView(mav, response);
    }

    private static void renderDynamicView(ModelAndView mav, Response response) {
        try{
            Path filePath = Paths.get("./src/main/resources/static" + mav.getViewName());
            String bodyString = Files.readString(filePath, StandardCharsets.UTF_8);

            logger.debug("bodyString = {} " , bodyString);

            for(String name : mav.getModelNames()){
                String toReplace = "\\$\\{\\{" + name + "\\}\\}";
                logger.debug("toReplace = {}", toReplace);
                bodyString = bodyString.replaceAll(toReplace, mav.getModelAttribute(name));
            }

            byte[] body = bodyString.getBytes(StandardCharsets.UTF_8);
            String contentType = FileMimeType.resolveMimeType(mav.getViewName());
            response.ok(body, contentType);
            return;
        }
        catch (IOException e) {
            logger.error("error occurred while reading static resource");
        }
        response.notFound();
    }

    private static void renderStaticView(ModelAndView mav, Response response) {
        try{
            byte[] body = Files.readAllBytes(new File("./src/main/resources/static" + mav.getViewName()).toPath());
            String contentType = FileMimeType.resolveMimeType(mav.getViewName());
            response.ok(body, contentType);
            return;
        }
        catch (IOException e) {
            logger.error("error occurred while reading static resource");
        }

        response.notFound();
    }
}
