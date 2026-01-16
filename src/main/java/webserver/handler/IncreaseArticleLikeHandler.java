package webserver.handler;

import db.ArticleRepository;
import model.Article;
import webserver.exception.BusinessException;
import webserver.http.Request;
import webserver.http.Response;
import webserver.mvc.Handler;
import webserver.mvc.ModelAndView;
import webserver.view.RedirectView;

public class IncreaseArticleLikeHandler implements Handler {
    private final ArticleRepository articleRepository;

    public IncreaseArticleLikeHandler(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public ModelAndView handle(Request request, Response response) {
        String articleId = request.getParameter("articleId");

        long id = Long.parseLong(articleId);

        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new BusinessException());

        article.increaseLikeCount();

        articleRepository.update(article);

        return new RedirectView("/");
    }
}
