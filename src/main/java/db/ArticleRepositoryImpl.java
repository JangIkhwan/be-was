package db;

import model.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticleRepositoryImpl implements ArticleRepository {
    private static final String JDBC_URL = "jdbc:h2:tcp://localhost/~/h2-db/was-be";
    private static final String USER = "sa";
    private static final String PASSWORD = "sa";
    private static final Logger logger = LoggerFactory.getLogger(ArticleRepositoryImpl.class);

    public Article save(Article article) {
        String sql = "insert into article_tbl(creatorId, title, content) values(?, ?, ?)";
        try (
                Connection con = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
                PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {
            pstmt.setLong(1, article.getCreatorId());
            pstmt.setString(2, article.getTitle());
            pstmt.setString(3, article.getContent());

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
    public List<Article> findTopNLessThanByIdDecreasingOrder(int limit, int id) {
        String sql = "select id, creatorId, title, content from article_tbl where id < ? order by id desc limit ?";

        try (
                Connection con = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            List<Article> articles = new ArrayList<>();

            pstmt.setLong(1, id);
            pstmt.setLong(2, limit);

            try (ResultSet rs = pstmt.executeQuery()) {
                while(!rs.next()) {
                    long articleId = rs.getLong("id");
                    long creatorId = rs.getLong("creatorId");
                    String title = rs.getString("title");
                    String content = rs.getString("content");
                    articles.add(new Article(articleId, creatorId, title, content));
                }
            }
            return articles;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
