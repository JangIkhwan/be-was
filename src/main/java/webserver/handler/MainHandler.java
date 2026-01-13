package webserver.handler;

import db.ArticleRepository;
import db.UserRepository;
import model.Article;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.Request;
import webserver.http.Response;
import webserver.mvc.*;
import webserver.util.AuthUtil;

import java.util.HashMap;
import java.util.List;
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
        if(loginUser != null){
            logger.debug("session found");
            model.put("name", loginUser.getName());
        }

        List<Article> latests = articleRepository.findTopNLessThanByIdDecreasingOrder(1, 100L);
        if(!latests.isEmpty()){
            logger.debug("found latest article");
            Article article = latests.get(0);
            User user = userRepository.findById(article.getCreatorId())
                    .orElseThrow(() -> new RuntimeException());
            article.setWriterName(user.getName());
            model.put("article", article);
        }

        return new MainPageDynamicView(model,"/index_logined.html");
    }
}
