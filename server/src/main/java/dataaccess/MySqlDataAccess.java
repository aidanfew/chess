package dataaccess;

import io.javalin.http.HttpResponseException;
import model.UserData;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.xml.crypto.Data;
import java.sql.*;


public class MySqlDataAccess {
    private Connection connection;

    public MySqlDataAccess() throws SQLException {
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

    private final String[] createAuthTable = {
            """
    CREATE TABLE IF NOT EXISTS auths (
    'authToken' INT NOT NULL,
    'username' VARCHAR(256) NOT NULL,
    PRIMARY KEY (authToken) )
"""
    };

    private final String[] createGameTable = {
            """
    CREATE TABLE IF NOT EXISTS games (
    'gameID' INT NOT NULL AUTO_INCREMENT,
    'whiteUsername' VARCHAR(256) NOT NULL,
    'blackUsername' VARCHAR(256) NOT NULL,
    'gameName' VARCHAR(256) NOT NULL,
    'game' CHESS GAME NOT NULL,
    PRIMARY KEY (gameID) )
"""
    };

    public void createSqlUser(UserData userData) throws SQLException {
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
    }

    public UserData getSqlUSer(String username) {
        String sql = "SELECT username, password, email FROM users WHERE username=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return new UserData(rs.getString(1), rs.getString(2), rs.getString(3));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




    private void configureDatabase() throws HttpResponseException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new HttpResponseException();
        }
    }
}
