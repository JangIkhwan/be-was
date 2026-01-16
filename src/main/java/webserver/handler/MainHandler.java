package webserver.handler;

import db.ArticleRepository;
import db.UserRepository;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.BusinessException;
import webserver.http.Request;
import webserver.http.Response;
import webserver.mvc.*;
import webserver.util.AuthUtil;
import webserver.view.MainPageDynamicView;
import webserver.mvc.ModelAndView;

import java.util.HashMap;
import java.util.Map;

public class MainHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(MainHandler.class);
    private ArticleRepository articleRepository;
    private UserRepository userRepository;

    public MainHandler(ArticleRepository articleRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    public ModelAndView handle(Request request, Response response) {
        Map<String, Object> model = new HashMap<>();

        User loginUser = AuthUtil.getAuthenticatedUser(request);
        if (loginUser != null) {
            logger.debug("session found");

            User user = userRepository.findById(loginUser.getId())
                    .orElseThrow(() -> new BusinessException());
            model.put("name", user.getName());
        }

        if (hasArticleId(request)) {
            try{
                long articleId = getArticleId(request);
                findOne(articleId, model);
            }
            catch(NumberFormatException e){
                findLatest(model);
            }
        } else {
            findLatest(model);
        }

        return new MainPageDynamicView(model, "/index_logined.html");
    }

    private void findOne(long articleId, Map<String, Object> model) {
        articleRepository.findById(articleId).ifPresent(article -> {
            logger.debug("found certain article");

            User user = userRepository.findById(article.getCreatorId())
                    .orElseThrow(() -> new BusinessException());

            article.setWriterName(user.getName());

            model.put("article", article);
            if (user.getImageUrl() != null) {
                model.put("writer_profile_image", user.getImageUrl());
            }

            articleRepository.findNext(article.getId()).ifPresent(next -> {
                model.put("next_article", next);
            });

            articleRepository.findPrev(article.getId()).ifPresent(prev -> {
                model.put("prev_article", prev);
            });
        });
    }

    private void findLatest(Map<String, Object> model) {
        articleRepository.findLatest().ifPresentOrElse(article -> {
                    logger.debug("found latest article");

                    User user = userRepository.findById(article.getCreatorId())
                            .orElseThrow(() -> new BusinessException());

                    article.setWriterName(user.getName());

                    model.put("article", article);

                    if (user.getImageUrl() != null) {
                        model.put("writer_profile_image", user.getImageUrl());
                    }

                    articleRepository.findNext(article.getId()).ifPresent(next -> {
                        logger.debug("added next");
                        model.put("next_article", next);
                    });

                    articleRepository.findPrev(article.getId()).ifPresent(prev -> {
                        logger.debug("added prev");
                        model.put("prev_article", prev);
                    });
                },
                () -> {
                    model.put("message", "첫번째 게시글을 작성해주세요");
                });
    }

    private boolean hasArticleId(Request request) {
        return request.getParameter("articleId") != null;
    }

    private long getArticleId(Request request) {
        String articleId = request.getParameter("articleId");
        return Integer.parseInt(articleId);
    }
}
