package webserver.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.constant.FileMimeType;
import webserver.exception.StaticResourceNotFoundException;
import webserver.http.Response;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class ModelAndDynamicView implements ModelAndView{
    private static final Logger logger = LoggerFactory.getLogger(ModelAndDynamicView.class);
    private Map<String, String> model;
    private String viewName;

    public ModelAndDynamicView(Map<String, String> model, String viewName) {
        this.model = model;
        this.viewName = viewName;
    }

    @Override
    public String getViewName() {
        return viewName;
    }

    @Override
    public void render(Response response) {
        try{
            Path filePath = Paths.get("./src/main/resources/static" + getViewName());
            String bodyString = Files.readString(filePath, StandardCharsets.UTF_8);

            logger.debug("bodyString = {} " , bodyString);

            for(String name : model.keySet()){
                String toReplace = "\\$\\{\\{" + name + "\\}\\}";
                logger.debug("toReplace = {}", toReplace);
                bodyString = bodyString.replaceAll(toReplace, model.get(name));
            }

            byte[] body = bodyString.getBytes(StandardCharsets.UTF_8);
            String contentType = FileMimeType.resolveMimeType(getViewName());
            response.setOk(body, contentType);
        }
        catch (IOException e) {
            logger.error("error occurred while reading static resource");
            throw new StaticResourceNotFoundException();
        }
    }
}
