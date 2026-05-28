package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.*;


public class UserSqlDAO {

    public UserSqlDAO() {
    }

    public String hashUserPassword(String clearTextPassword) {
        return BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
    }


    public void createUser(UserData userData) throws DataAccessException {
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        String hashedPassword = hashUserPassword(userData.password());
        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, userData.username());
                stmt.setString(2, hashedPassword);
                stmt.setString(3, userData.email());
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            String message = "Error: connection error";
            System.out.println(e);
            throw new DataAccessException(message, 500);
        }
    }

    public UserData getUser(String username) throws DataAccessException {
        String sql = "SELECT username, password, email FROM users WHERE username=?";
        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return new UserData(rs.getString(1), rs.getString(2), rs.getString(3));
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            String message = "Error: connection error";
            System.out.println(e);
            throw new DataAccessException(message, 500);
        }
    }

    public void clear() throws DataAccessException {
        String sql = "TRUNCATE TABLE users";
        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            String message = "Error: connection error";
            System.out.println(e);
            throw new DataAccessException(message, 500);
        }
    }
}
