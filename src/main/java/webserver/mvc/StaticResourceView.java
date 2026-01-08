package webserver.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.constant.FileMimeType;
import webserver.exception.StaticResourceNotFoundException;
import webserver.http.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Set;

public class StaticResourceView implements ModelAndView{
    private static final Logger logger = LoggerFactory.getLogger(StaticResourceView.class);
    private String viewName;

    public StaticResourceView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public ModelAndView addModelAttribute(String name, String value) {
        return this;
    }

    @Override
    public String getViewName() {
        return viewName;
    }

    @Override
    public Set<String> getModelNames() {
        return Set.of();
    }

    @Override
    public String getModelAttribute(String name) {
        return "";
    }

    @Override
    public void render(Response response) {
        try{
            byte[] body = Files.readAllBytes(new File("./src/main/resources/static" + getViewName()).toPath());
            String contentType = FileMimeType.resolveMimeType(getViewName());
            response.setOk(body, contentType);
        }
        catch (IOException e) {
            logger.error("error occurred while reading static resource");
            throw new StaticResourceNotFoundException();
        }
    }
}
