package webserver.mvc;

import model.Article;
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

public class MainPageDynamicView implements ModelAndView {
    private static final Logger logger = LoggerFactory.getLogger(MainPageDynamicView.class);
    private Map<String, Object> model;
    private String viewName;

    public MainPageDynamicView(Map<String, Object> model, String viewName) {
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
            Path filePath = Paths.get("./src/main/resources/static" + getViewName());
            String baseHtml = Files.readString(filePath, StandardCharsets.UTF_8);

            logger.debug("before baseHtml = {} ", baseHtml);

            if (model.containsKey("name")) {
                Path templatePath = Paths.get("./src/main/resources/static" + "/template/header_menu_logined.html");
                String headerMenuTemplate = Files.readString(templatePath, StandardCharsets.UTF_8);
                logger.debug("headerMenuTemplate= {}", headerMenuTemplate);
                headerMenuTemplate = headerMenuTemplate.replace("${{name}}", (String) model.get("name"));
                baseHtml = baseHtml.replace("${{header_menu}}", headerMenuTemplate);
            } else {
                Path templatePath = Paths.get("./src/main/resources/static" + "/template/header_menu_public.html");
                String headerMenuTemplate = Files.readString(templatePath, StandardCharsets.UTF_8);
                logger.debug("headerMenuTemplate= {}", headerMenuTemplate);
                baseHtml = baseHtml.replace("${{header_menu}}", headerMenuTemplate);
            }

            if (model.containsKey("article")) {
                Article article = (Article) model.get("article");
                Path templatePath = Paths.get("./src/main/resources/static" + "/template/article.html");
                String articleTemplate = Files.readString(templatePath, StandardCharsets.UTF_8);
                logger.debug("articleTemplate = {}", articleTemplate);

                articleTemplate = articleTemplate.replace("${{writer}}", article.getWriterName());
                articleTemplate = articleTemplate.replace("${{content}}", article.getContent());
                articleTemplate = articleTemplate.replace("${{image_url}}", article.getImageUrl());

                if (!model.containsKey("comments")) {
                    articleTemplate = articleTemplate.replace("${{comments}}", "댓글이 없습니다");
                    articleTemplate = articleTemplate.replace("${{all-comments-button}}", "");
                }

                baseHtml = baseHtml.replace("${{article}}", articleTemplate);

            } else {
                baseHtml = baseHtml.replace("${{article}}", "첫 게시글을 써주세요");
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
