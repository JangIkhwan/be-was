package webserver.handler;

import db.ArticleRepository;
import model.Article;
import model.User;
import webserver.http.Request;
import webserver.http.Response;
import webserver.mvc.Handler;
import webserver.mvc.ModelAndView;
import webserver.mvc.RedirectView;
import webserver.util.AuthUtil;

public class CreateArticleHandler implements Handler {
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

        articleRepository.save(new Article(user.getId(), title, content));

        return new RedirectView("/");
    }
}
