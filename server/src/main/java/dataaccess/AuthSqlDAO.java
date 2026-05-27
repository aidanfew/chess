package dataaccess;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import io.javalin.http.HttpResponseException;
import model.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthSqlDAO {
    private Connection connection;

    public AuthSqlDAO() throws Exception {
        configureDatabase();
    }

    private final String[] createAuthTable = {
            """
    CREATE TABLE IF NOT EXISTS auths (
    'authToken' INT NOT NULL,
    'username' VARCHAR(256) NOT NULL,
    PRIMARY KEY (authToken) )
"""
    };

    public static String generateToken() { return UUID.randomUUID().toString(); }

    public void createAuth(AuthData authData) throws Exception {
        String sql = "INSERT INTO auths (authToken, username) VALUES (?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, authData.authToken());
            stmt.setString(2, authData.username());
            stmt.executeUpdate();
        }
    }

    public AuthData getAuth(String authToken) throws Exception {
        String sql = "SELECT authToken, username FROM auths WHERE authToken=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, authToken);
            ResultSet rs = stmt.executeQuery();
            return new AuthData(rs.getString(1), rs.getString(2));
        }
    }

    public void deleteAuth(String authToken) throws Exception {
        String sql = "DELETE FROM auths WHERE authToken=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, authToken);
            stmt.executeUpdate();
        }
    }


    private void configureDatabase() throws HttpResponseException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createAuthTable) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (DataAccessException e) {

        }
    }
}
