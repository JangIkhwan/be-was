package db;

import model.Article;

public interface ArticleRepository {
    Article save(Article article);
}
