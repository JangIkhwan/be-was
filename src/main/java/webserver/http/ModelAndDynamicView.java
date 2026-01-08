package webserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.constant.FileMimeType;
import webserver.exception.StaticResourceNotFoundException;
import webserver.handler.Response;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ModelAndDynamicView implements ModelAndView{
    private static final Logger logger = LoggerFactory.getLogger(ModelAndDynamicView.class);
    private Map<String, String> model = new HashMap<>();
    private String viewName;

    public ModelAndDynamicView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public ModelAndView addModelAttribute(String name, String value) {
        model.put(name, value);
        return this;
    }

    @Override
    public String getViewName() {
        return viewName;
    }

    @Override
    public Set<String> getModelNames() {
        return model.keySet();
    }

    @Override
    public String getModelAttribute(String name) {
        return model.get(name);
    }

    @Override
    public void render(Response response) {
        try{
            Path filePath = Paths.get("./src/main/resources/static" + getViewName());
            String bodyString = Files.readString(filePath, StandardCharsets.UTF_8);

            logger.debug("bodyString = {} " , bodyString);

            for(String name : getModelNames()){
                String toReplace = "\\$\\{\\{" + name + "\\}\\}";
                logger.debug("toReplace = {}", toReplace);
                bodyString = bodyString.replaceAll(toReplace, getModelAttribute(name));
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
