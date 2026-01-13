package db;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class UserRepository {
    private static final String JDBC_URL = "jdbc:h2:tcp://localhost/~/h2-db/was-be";
    private static final String USER = "sa";
    private static final String PASSWORD = "sa";
    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    public User save(User user){
        String sql = "insert into user_tbl(password, nickname, email) values(?, ?, ?)";

        try (
            Connection con = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ){
            logger.debug("DB 연결 성공");

            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());

            pstmt.executeUpdate();

            try(ResultSet rs = pstmt.getGeneratedKeys();){
                if (!rs.next()) {
                    throw new SQLException("id 조회 실패");
                }
                user.setId(rs.getLong(1));
            }
            return user;

        }
        catch (Exception e) {
            logger.error("DB 연결 에러", e);
            throw new RuntimeException(e);
        }
    }
}
