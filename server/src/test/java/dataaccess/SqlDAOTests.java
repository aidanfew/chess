package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import results.ListGamesHelperResult;
import service.ClearService;
import service.GameService;
import service.UserService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;

public class SqlDAOTests {
    UserService user = new UserService();
    GameService game = new GameService();
    AuthSqlDAO auth = new AuthSqlDAO();
    UserSqlDAO userSqlDAO = new UserSqlDAO();
    GameSqlDAO gameSqlDAO = new GameSqlDAO();
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
    @DisplayName("getAuth Positive Test")
    public void positiveGetAuth() throws Exception {
        clearAll();
        AuthData authData = new AuthData("1234", "user1");
        auth.createAuth(authData);
        AuthData newAuthData = auth.getAuth("1234");
        Assertions.assertNotNull(newAuthData);
    }

    @Test
    @DisplayName("getAuth Negative Test")
    public void negativeGetAuth() throws Exception {
        clearAll();
        AuthData authData = new AuthData("1234", "user1");
        auth.createAuth(authData);
        AuthData newAuthData = auth.getAuth("1235");
        Assertions.assertNull(newAuthData);
    }

    @Test
    @DisplayName("deleteAuth Positive Test")
    public void positiveDeleteAuth() throws Exception {
        clearAll();
        Connection connection = DatabaseManager.getConnection();
        AuthData authData = new AuthData("1234", "user1");
        auth.createAuth(authData);
        String sql = "SELECT COUNT(*) from auths";
        auth.deleteAuth("1234");
        Assertions.assertTrue(check.countZero(sql, connection));
    }

    @Test
    @DisplayName("deleteAuth Negative Test")
    public void negativeDeleteAuth() throws Exception {
        clearAll();
        Connection connection = DatabaseManager.getConnection();
        AuthData authData = new AuthData("1234", "user1");
        auth.createAuth(authData);
        String sql = "SELECT COUNT(*) from auths";
        auth.deleteAuth("1235");
        Assertions.assertFalse(check.countZero(sql, connection));
    }

    @Test
    @DisplayName("createUser Positive Test")
    public void positiveCreateUser() throws Exception {
        clearAll();
        Connection connection = DatabaseManager.getConnection();
        UserData userData = new UserData("user1", "1234", "user1@gmail.com");
        userSqlDAO.createUser(userData);
        String sql = "SELECT COUNT(*) from users";
        Assertions.assertFalse(check.countZero(sql, connection));
    }

    @Test
    @DisplayName("createUser Negative Test")
    public void negativeCreatesUser() throws Exception {
        clearAll();
        UserData userData = new UserData("user1", "1234", null);
        Assertions.assertThrows(DataAccessException.class, () -> {
            userSqlDAO.createUser(userData);
        });
    }

    @Test
    @DisplayName("getUser Positive Test")
    public void positiveGetUser() throws Exception {
        clearAll();
        UserData userData = new UserData("user1", "1234", "user1@gmail.com");
        userSqlDAO.createUser(userData);
        UserData newUserData = userSqlDAO.getUser("user1");
        Assertions.assertNotNull(newUserData);
    }

    @Test
    @DisplayName("getUser Negative Test")
    public void negativeGetUser() throws Exception {
        clearAll();
        UserData userData = new UserData("user1", "1234", "user1@gmail.com");
        userSqlDAO.createUser(userData);
        UserData newUserData = userSqlDAO.getUser("user2");
        Assertions.assertNull(newUserData);
    }

    @Test
    @DisplayName("createGame Positive Test")
    public void positiveCreateGame() throws Exception {
        clearAll();
        Connection connection = DatabaseManager.getConnection();
        String sql = "SELECT COUNT(*) from games";
        gameSqlDAO.createGame("game1");
        Assertions.assertFalse(check.countZero(sql, connection));
    }

    @Test
    @DisplayName("createGame Negative Test")
    public void negativeCreateGame() throws Exception {
        clearAll();
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameSqlDAO.createGame(null);
        });
    }

    @Test
    @DisplayName("currentGameID Positive Test")
    public void positiveCurrentGameID() throws Exception {
        clearAll();
        gameSqlDAO.createGame("game1");
        int gameID = gameSqlDAO.currentGameID();
        Assertions.assertEquals(1, gameID);
    }

    @Test
    @DisplayName("currentGameID Negative Test")
    public void negativeCurrentGameID() throws Exception {
        clearAll();
        int gameID = gameSqlDAO.currentGameID();
        Assertions.assertEquals(0, gameID);
    }

    @Test
    @DisplayName("getGame Positive Test")
    public void positiveGetGame() throws Exception {
        clearAll();
        gameSqlDAO.createGame("game1");
        GameData gameData = gameSqlDAO.getGame(1);
        Assertions.assertNotNull(gameData);
    }

    @Test
    @DisplayName("getGame Negative Test")
    public void negativeGetGame() throws Exception {
        clearAll();
        gameSqlDAO.createGame("game1");
        GameData gameData = gameSqlDAO.getGame(2);
        Assertions.assertNull(gameData);
    }

    @Test
    @DisplayName("replaceGame Positive Test")
    public void positiveReplaceGame() throws Exception {
        clearAll();
        gameSqlDAO.createGame("game1");
        GameData newData = new GameData(1, "user1", null, "game1", new ChessGame());
        gameSqlDAO.replaceGame(1, null, newData);
        String sql = "SELECT whiteUsername FROM games WHERE gameID = 1";
        Connection connection = DatabaseManager.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            String whiteUsername = rs.getString(1);
            Assertions.assertEquals("user1", whiteUsername);
        }
    }

    @Test
    @DisplayName("replaceGame Negative Test")
    public void negativeReplaceGame() throws Exception {
        clearAll();
        gameSqlDAO.createGame("game1");
        GameData newData = new GameData(1, "user1", null, "game1", new ChessGame());
        gameSqlDAO.replaceGame(2, null, newData);
        String sql = "SELECT whiteUsername FROM games WHERE gameID = 1";
        Connection connection = DatabaseManager.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            String whiteUsername = rs.getString(1);
            Assertions.assertNull(whiteUsername);
        }
    }

    @Test
    @DisplayName("createHelperList Positive Test")
    public void positiveCreateHelperList() throws Exception {
        clearAll();
        gameSqlDAO.createGame("game1");
        Collection<ListGamesHelperResult> list = gameSqlDAO.createHelperList();
        Assertions.assertFalse(list.isEmpty());
    }

    @Test
    @DisplayName("createHelperList Negative Test")
    public void negativeCreateHelperList() throws Exception {
        clearAll();
        Collection<ListGamesHelperResult> list = gameSqlDAO.createHelperList();
        Assertions.assertTrue(list.isEmpty());
    }
}
