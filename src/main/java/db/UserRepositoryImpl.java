package db;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    private static final String JDBC_URL = "jdbc:h2:tcp://localhost/~/h2-db/was-be";
    private static final String USER = "sa";
    private static final String PASSWORD = "sa";
    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    public User save(User user) {
        String sql = "insert into user_tbl(password, nickname, email) values(?, ?, ?)";

        try (
                Connection con = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
                PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {
            logger.debug("DB 연결 성공");

            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys();) {
                if (!rs.next()) {
                    throw new SQLException("id 조회 실패");
                }
                user.setId(rs.getLong(1));
            }
            return user;

        } catch (Exception e) {
            logger.error("DB 연결 에러", e);
            throw new RuntimeException(e);
        }
    }

    public Optional<User> findByEmail(String email) {
        String sql = "select id, password, nickname from user_tbl where email = ?";

        try (
                Connection con = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            logger.debug("DB 연결 성공");

            pstmt.setString(1, email);

            User user = null;
            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                long id = rs.getLong("id");
                String password = rs.getString("password");
                String nickname = rs.getString("nickname");
                user = new User(id, password, nickname, email);
            }
            return Optional.of(user);

        } catch (SQLException e) {
            logger.debug("DB 연결 실패", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "select password, nickname, email from user_tbl where id = ?";

        try (
                Connection con = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            logger.debug("DB 연결 성공");

            pstmt.setLong(1, id);

            User user = null;
            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                String password = rs.getString("password");
                String nickname = rs.getString("nickname");
                String email = rs.getString("email");
                user = new User(id, password, nickname, email);
            }
            return Optional.of(user);

        } catch (SQLException e) {
            logger.debug("DB 연결 실패", e);
            throw new RuntimeException(e);
        }
    }
}