package webserver.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.constant.FileMimeType;
import webserver.exception.StaticResourceNotFoundException;
import webserver.http.Response;
import webserver.mvc.ModelAndView;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class FormErrorDynamicView implements ModelAndView {
    private static final Logger logger = LoggerFactory.getLogger(FormErrorDynamicView.class);
    private String viewName;
    private Map<String, Object> model;

    public FormErrorDynamicView(String viewName, Map<String, Object> model) {
        this.viewName = viewName;
        this.model = model;
    }

    @Override
    public String getViewName() {
        return viewName;
    }

    @Override
    public void render(Response response) {
        try {
            Path filePath = Paths.get("./src/main/resources/static" + viewName);
            String baseHtml = Files.readString(filePath, StandardCharsets.UTF_8);

            logger.debug("before baseHtml = {} ", baseHtml);

            if (model.containsKey("error")) {
                baseHtml = baseHtml.replace("${{error}}", (String) model.get("error"));
            }

            logger.debug("after baseHtml = {} ", baseHtml);

            byte[] body = baseHtml.getBytes(StandardCharsets.UTF_8);
            String contentType = FileMimeType.resolveMimeType(getViewName());
            response.setOk(body, contentType);
        } catch (IOException e) {
            logger.error("error occurred while reading static resource");
            throw new StaticResourceNotFoundException();
        }
    }
}
