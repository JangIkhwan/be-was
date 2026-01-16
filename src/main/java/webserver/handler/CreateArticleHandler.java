package webserver.handler;

import db.ArticleRepository;
import model.Article;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.Request;
import webserver.http.Response;
import webserver.mvc.*;
import webserver.util.AuthUtil;
import webserver.mvc.ModelAndView;
import webserver.util.MultipartFileUtil;
import webserver.view.RedirectView;
import webserver.view.StaticResourceView;

import java.io.IOException;

public class CreateArticleHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(CreateArticleHandler.class);
    private final ArticleRepository articleRepository;

    public CreateArticleHandler(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public ModelAndView handle(Request request, Response response) {
        if(!AuthUtil.isAuthenticatedUser(request)){
            return new RedirectView("/login");
        }
        User user = AuthUtil.getAuthenticatedUser(request);
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        if(request.getMultipartFiles().size() != 1){
            logger.debug("글 생성 요청에 이미지 없음");
            return new StaticResourceView("/article/error.html");
        }

        MultipartFile multipartFile = request.getMultipartFiles().get(0);

        String imageUrl = null;
        try {
            imageUrl = MultipartFileUtil.saveFile("uploads/images", multipartFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        articleRepository.save(new Article(user.getId(), title, content, imageUrl));

        return new RedirectView("/");
    }
}
