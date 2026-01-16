package db;

import model.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArticleRepositoryImpl implements ArticleRepository {
    private static final String JDBC_URL = "jdbc:h2:tcp://localhost/~/h2-db/was-be";
    private static final String USER = "sa";
    private static final String PASSWORD = "sa";
    private static final Logger logger = LoggerFactory.getLogger(ArticleRepositoryImpl.class);

    public Article save(Article article) {
        String sql = "insert into article_tbl(creatorId, title, content, image_url) values(?, ?, ?, ?)";
        try (
                Connection con = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
                PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {
            pstmt.setLong(1, article.getCreatorId());
            pstmt.setString(2, article.getTitle());
            pstmt.setString(3, article.getContent());
            pstmt.setString(4, article.getImageUrl());

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (!rs.next()) {
                    throw new SQLException("조회 실패");
                }
                long id = rs.getLong(1);
                article.setId(id);
            }
            return article;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Article> findTopNLessThanByIdDecreasingOrder(int limit, long id) {
        String sql = "select id, creatorId, title, content, image_url, like_count from article_tbl where id < ? order by id desc limit ?";

        try (
                Connection con = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            List<Article> articles = new ArrayList<>();

            pstmt.setLong(1, id);
            pstmt.setLong(2, limit);

            try (ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    long articleId = rs.getLong("id");
                    long creatorId = rs.getLong("creatorId");
                    String title = rs.getString("title");
                    String content = rs.getString("content");
                    String imageUrl = rs.getString("image_url");
                    long likeCount = rs.getLong("like_count");
                    articles.add(new Article(articleId, creatorId, title, content, imageUrl, likeCount));
                }
            }
            return articles;
        } catch (SQLException e) {
            logger.error("DB 접속 실패", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Article> findById(long id) {
        String sql = "select id, creatorId, title, content, image_url, like_count " +
                "from article_tbl where id = ?";

        try (
                Connection con = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }

                Article article = new Article(
                        rs.getLong("id"),
                        rs.getLong("creatorId"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("image_url"),
                        rs.getLong("like_count")
                );

                return Optional.of(article);
            }
        } catch (SQLException e) {
            logger.error("Article 조회 실패 id={}", id, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Article article) {
        String sql = "update article_tbl " +
                "set title = ?, content = ?, image_url = ?, like_count = ? " +
                "where id = ?";

        try (
                Connection con = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            pstmt.setString(1, article.getTitle());
            pstmt.setString(2, article.getContent());
            pstmt.setString(3, article.getImageUrl());
            pstmt.setLong(4, article.getLikeCount());
            pstmt.setLong(5, article.getId());

            int updatedRows = pstmt.executeUpdate();
            if (updatedRows == 0) {
                throw new SQLException("업데이트 실패 - 존재하지 않는 article id=" + article.getId());
            }
        } catch (SQLException e) {
            logger.error("Article 업데이트 실패 id={}", article.getId(), e);
            throw new RuntimeException(e);
        }
    }
}
