package db;

import model.Article;

import java.util.List;

public interface ArticleRepository {
    Article save(Article article);
    List<Article> findTopNLessThanByIdDecreasingOrder(int limit, long id);
}
