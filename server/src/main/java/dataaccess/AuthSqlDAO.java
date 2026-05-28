package dataaccess;

import model.AuthData;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthSqlDAO {
    private final Connection connection;

    public AuthSqlDAO() throws DataAccessException {
        this.connection = DatabaseManager.getConnection();
    }


    public static String generateToken() { return UUID.randomUUID().toString(); }

    public void createAuth(AuthData authData) throws Exception {
        String sql = "INSERT INTO auths (authToken, username) VALUES (?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, authData.authToken());
            stmt.setString(2, authData.username());
            stmt.executeUpdate();
        } catch (Exception e) {
            String message = "Error: Connection error";
            System.out.println(e);
            throw new DataAccessException(message, 500);
        }
    }

    public AuthData getAuth(String authToken) throws Exception {
        String sql = "SELECT authToken, username FROM auths WHERE authToken=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, authToken);
            ResultSet rs = stmt.executeQuery();
            return new AuthData(rs.getString(1), rs.getString(2));
        } catch (Exception e) {
            String message = "Error: connection error";
            System.out.println(e);
            throw new DataAccessException(message, 500);
        }
    }

    public void deleteAuth(String authToken) throws Exception {
        String sql = "DELETE FROM auths WHERE authToken=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, authToken);
            stmt.executeUpdate();
        } catch (Exception e) {
            String message = "Error: connection error";
            System.out.println(e);
            throw new DataAccessException(message, 500);
        }
    }

    public void clear() throws Exception{
        String sql = "DROP TABLE auths";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (Exception e) {
            String message = "Error: connection error";
            System.out.println(e);
            throw new DataAccessException(message, 500);
        }
    }
}
