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

public class MyPageDynamicView implements ModelAndView {
    private static final Logger logger = LoggerFactory.getLogger(MyPageDynamicView.class);
    private Map<String, Object> model;
    private String viewName;

    public MyPageDynamicView(Map<String, Object> model, String viewName) {
        this.model = model;
        this.viewName = viewName;
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

            if (model.containsKey("name")) {
                baseHtml = baseHtml.replace("${{name}}", (String) model.get("name"));
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
