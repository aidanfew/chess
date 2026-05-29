package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requests.*;
import results.ListGamesHelperResult;
import results.ListGamesResult;

import javax.xml.crypto.Data;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.sql.Connection;


public class ServiceTests {
    UserService user = new UserService();
    GameService game = new GameService();
    AuthSqlDAO authDAO = new AuthSqlDAO();
    GameSqlDAO gameSqlDAO = new GameSqlDAO();
    TestsHelper check = new TestsHelper();

    public ServiceTests() throws DataAccessException {
    }


    public void clearAll() throws Exception {
        ClearService cleared = new ClearService();
        cleared.clear(user, game, authDAO);
    }

    @Test
    @DisplayName("Test Clear")
    public void clearAllSuccess() throws Exception {
        clearAll();
        Connection connection = DatabaseManager.getConnection();
        String sql = "SELECT COUNT(*) FROM users, games, auths";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            int count = rs.getInt(1);
            Assertions.assertEquals(0, count);
        }
    }

    @Test
    @DisplayName("Positive Register Test")
    public void positiveRegister() throws Exception {
        clearAll();
        Connection connection = DatabaseManager.getConnection();
        RegisterRequest registerRequest = new RegisterRequest("user1", "pass1", "email@email.com");
        user.register(registerRequest, authDAO);
        String sql = "SELECT COUNT(*) FROM users";
        Assertions.assertFalse(check.countZero(sql, connection));
    }

    @Test
    @DisplayName("Negative Register Test: Null Password")
    public void negativeRegister() throws Exception {
        clearAll();
        Assertions.assertThrows(DataAccessException.class, () -> {
            RegisterRequest registerRequest = new RegisterRequest("user1", null, "email@email.com");
            user.register(registerRequest, authDAO);
        });
    }

    @Test
    @DisplayName("Positive Login Test")
    public void positiveLogin() throws Exception {
        clearAll();
        RegisterRequest registerRequest = new RegisterRequest("user1", "1234", "email@email.com");
        user.register(registerRequest, authDAO);
        LoginRequest loginRequest = new LoginRequest("user1", "1234");
        user.login(loginRequest, authDAO);
        String sql = "SELECT COUNT(*) FROM auths";
        Connection connection = DatabaseManager.getConnection();
        Assertions.assertFalse(check.countZero(sql, connection));
    }

    @Test
    @DisplayName("Negative Login Test: Wrong Password")
    public void negativeLogin() throws Exception {
        clearAll();
        Assertions.assertThrows(DataAccessException.class, () -> {
            RegisterRequest registerRequest = new RegisterRequest("user1", "1234", "email@email.com");
            user.register(registerRequest, authDAO);
            LoginRequest loginRequest = new LoginRequest("user1", "1235");
            user.login(loginRequest, authDAO);
        });
    }

    @Test
    @DisplayName("Positive Logout Test")
    public void positiveLogout() throws Exception {
        clearAll();
        AuthData authData = new AuthData("1234", "user1");
        authDAO.createAuth(authData);
        user.logout("1234", authDAO);
        Connection connection = DatabaseManager.getConnection();
        String sql = "SELECT COUNT(*) from auths";
        Assertions.assertTrue(check.countZero(sql, connection));
    }

    @Test
    @DisplayName("Negative Logout Test: Wrong Token")
    public void negativeLogout() throws Exception {
        clearAll();
        Assertions.assertThrows(DataAccessException.class, () -> {
            AuthData authData = new AuthData("1234", "user1");
            authDAO.createAuth(authData);
            user.logout("1235", authDAO);
        });
    }

    @Test
    @DisplayName("Positive Create Game")
    public void positiveCreateGame() throws Exception {
        clearAll();
        authDAO.createAuth(new AuthData("1234", "user1"));
        CreateGameRequest request = new CreateGameRequest("game1", "1234");
        game.createGame(request, authDAO);
        Connection connection = DatabaseManager.getConnection();
        String sql = "SELECT COUNT(*) FROM games";
        Assertions.assertFalse(check.countZero(sql, connection));
    }

    @Test
    @DisplayName("Negative Create Game: Wrong Token")
    public void negativeCreateGame() throws Exception {
        clearAll();
        Assertions.assertThrows(DataAccessException.class, () -> {
            authDAO.createAuth(new AuthData("1234", "user1"));
            CreateGameRequest request = new CreateGameRequest("game1", "1235");
            game.createGame(request, authDAO);
        });
    }

    @Test
    @DisplayName("Positive Join Game")
    public void positiveJoinGame() throws Exception {
        clearAll();
        authDAO.createAuth(new AuthData("1234", "user1"));
        CreateGameRequest createGameRequest = new CreateGameRequest("game1", "1234");
        game.createGame(createGameRequest, authDAO);
        JoinGameRequest request = new JoinGameRequest("1234", "BLACK", 1);
        game.joinGame(request, authDAO);
        Connection connection = DatabaseManager.getConnection();
        String sql = "SELECT blackUsername FROM games WHERE gameID = 1";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            String blackUsername = rs.getString(1);
            Assertions.assertNotNull(blackUsername);
        }
    }

    @Test
    @DisplayName("Negative Join Game: Both Colors Taken")
    public void negativeJoinGame() throws Exception {
        clearAll();
        Assertions.assertThrows(DataAccessException.class, () -> {
            authDAO.createAuth(new AuthData("1234", "user1"));
            gameSqlDAO.createGame("game1");
            GameData gameData = new GameData(1, "user1", "user2", "game1", new ChessGame());
            gameSqlDAO.replaceGame(1, null, gameData);
            JoinGameRequest request = new JoinGameRequest("1234", "BLACK", 1);
            game.joinGame(request, authDAO);
        });
    }

    @Test
    @DisplayName("Positive List Games")
    public void positiveListGames() throws Exception {
        clearAll();
        authDAO.createAuth(new AuthData("1234", "user1"));
        gameSqlDAO.createGame("game1");
        ListGamesRequest request = new ListGamesRequest("1234");
        Collection<ListGamesHelperResult> response = game.listGames(request, authDAO);
        ListGamesResult list = new ListGamesResult(response);
        Assertions.assertFalse(list.games().isEmpty());
    }

    @Test
    @DisplayName("Negative List Games: Games is Empty")
    public void negativeListGames() throws Exception {
        clearAll();
        Assertions.assertThrows(DataAccessException.class, () -> {
            authDAO.createAuth(new AuthData("1234", "user1"));
            ListGamesRequest request = new ListGamesRequest("1235");
            Collection<ListGamesHelperResult> response = game.listGames(request, authDAO);
        });
    }
 }
