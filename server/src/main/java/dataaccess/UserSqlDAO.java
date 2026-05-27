package dataaccess;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import io.javalin.http.HttpResponseException;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.*;


public class UserSqlDAO {
    private Connection connection;

    public UserSqlDAO() throws SQLException {
        configureDatabase();
    }

    private final String[] createUserTable = {
    """
    CREATE TABLE IF NOT EXISTS users (
    'username' VARCHAR(256) NOT NULL,
    'password' VARCHAR(256) NOT NULL,
    'email' VARCHAR(256) NOT NULL,
    PRIMARY KEY (username) )
    """
    };

    public String hashUserPassword(String clearTextPassword) {
        return BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
    }


    public void createUser(UserData userData) throws SQLException {
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        String hashedPassword = hashUserPassword(userData.password());
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userData.username());
            stmt.setString(2, hashedPassword);
            stmt.setString(3, userData.email());
            stmt.executeUpdate();
        }
    }

    public UserData getUser(String username) throws SQLException {
        String sql = "SELECT username, password, email FROM users WHERE username=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return new UserData(rs.getString(1), rs.getString(2), rs.getString(3));
        }
    }



    private void configureDatabase() throws HttpResponseException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createUserTable) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        }
    }
}
