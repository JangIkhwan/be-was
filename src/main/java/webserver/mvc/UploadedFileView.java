package webserver.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.constant.FileMimeType;
import webserver.exception.StaticResourceNotFoundException;
import webserver.http.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class UploadedFileView implements ModelAndView {
    private static final Logger logger = LoggerFactory.getLogger(UploadedFileView.class);
    private String viewName;

    public UploadedFileView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public String getViewName() {
        return viewName;
    }

    @Override
    public void render(Response response) {
        try{
            String projectRoot = System.getProperty("user.dir");
            String pathname = projectRoot + getViewName();

            logger.debug("pathname : {}", pathname);

            byte[] body = Files.readAllBytes(new File(pathname).toPath());
            String contentType = FileMimeType.resolveMimeType(getViewName());
            response.setOk(body, contentType);
        }
        catch (IOException e) {
            logger.error("error occurred while reading static resource");
            throw new StaticResourceNotFoundException();
        }
    }
}
