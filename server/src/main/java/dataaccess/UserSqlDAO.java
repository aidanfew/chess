package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.*;


public class UserSqlDAO {
    private final Connection connection;

    public UserSqlDAO() throws Exception {
        this.connection = DatabaseManager.getConnection();
    }

    public String hashUserPassword(String clearTextPassword) {
        return BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
    }


    public void createUser(UserData userData) throws Exception {
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        String hashedPassword = hashUserPassword(userData.password());
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userData.username());
            stmt.setString(2, hashedPassword);
            stmt.setString(3, userData.email());
            stmt.executeUpdate();
        } catch (Exception e) {
            String message = "Error: connection error";
            System.out.println(e);
            throw new DataAccessException(message, 500);
        }
    }

    public UserData getUser(String username) throws Exception {
        String sql = "SELECT username, password, email FROM users WHERE username=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return new UserData(rs.getString(1), rs.getString(2), rs.getString(3));
        } catch (Exception e) {
            String message = "Error: connection error";
            System.out.println(e);
            throw new DataAccessException(message, 500);
        }
    }

    public void clear() throws Exception{
        String sql = "DROP TABLE users";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (Exception e) {
            String message = "Error: connection error";
            System.out.println(e);
            throw new DataAccessException(message, 500);
        }
    }
}
