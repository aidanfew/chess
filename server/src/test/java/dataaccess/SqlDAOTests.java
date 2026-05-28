package dataaccess;

import model.AuthData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.ClearService;
import service.GameService;
import service.UserService;
import dataaccess.TestsHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SqlDAOTests {
    UserService user = new UserService();
    GameService game = new GameService();
    AuthSqlDAO auth = new AuthSqlDAO();
    TestsHelper check = new TestsHelper();

    public SqlDAOTests() throws DataAccessException {
    }

    public void clearAll() throws Exception {
        ClearService cleared = new ClearService();
        cleared.clear(user, game, auth);
    }

    @Test
    @DisplayName("clearAll Success")
    public void clearAllSuccess() throws Exception {
        Connection connection = DatabaseManager.getConnection();
        clearAll();
        String sql = "SELECT COUNT(*) FROM users, games, auths";
        Assertions.assertTrue(check.countZero(sql, connection));
    }

    @Test
    @DisplayName("createAuth Positive Test")
    public void positiveCreateAuth() throws Exception {
        Connection connection = DatabaseManager.getConnection();
        clearAll();
        String sql = "SELECT COUNT(*) from auths";
        AuthData authData = new AuthData("12345", "user1");
        auth.createAuth(authData);
        Assertions.assertFalse(check.countZero(sql, connection));
    }

    @Test
    @DisplayName("createAuth Negative Test")
    public void negativeCreateAuth() throws Exception {
        clearAll();
        AuthData authData = new AuthData("12345", null);
        Assertions.assertThrows(DataAccessException.class, () -> {
            auth.createAuth(authData);
        });
    }

    @Test
    @DisplayName("")
    public void positiveGetAuth() throws Exception {
        clearAll();

    }


}
