package db;

import model.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository {
    Article save(Article article);

    List<Article> findTopNLessThanByIdDecreasingOrder(int limit, long id);

    Optional<Article> findById(long id);

    void update(Article article);

    Optional<Article> findLatest();

    Optional<Article> findNext(Long id);

    Optional<Article> findPrev(Long id);
}
