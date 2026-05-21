package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requests.*;
import results.ListGamesHelperResult;
import results.ListGamesResult;

import java.util.Collection;


public class ServiceTests {
    UserService user = new UserService();
    GameService game = new GameService();
    AuthDAO authDAO = new AuthDAO();

    public void clearAll() {
        ClearService cleared = new ClearService();
        cleared.clear(user, game, authDAO);
    }

    @Test
    @DisplayName("Test Clear")
    public void clearAllSuccess() {
        clearAll();
        Assertions.assertTrue(AuthDAO.authMap.isEmpty());
        Assertions.assertTrue(UserDAO.userMap.isEmpty());
        Assertions.assertTrue(GameDAO.gameMap.isEmpty());
    }

    @Test
    @DisplayName("Positive Register Test")
    public void positiveRegister() throws Exception {
        clearAll();
        RegisterRequest registerRequest = new RegisterRequest("user1", "pass1", "email@email.com");
        UserService service = new UserService();
        service.register(registerRequest, authDAO);
        Assertions.assertFalse(UserDAO.userMap.isEmpty());
    }

    @Test
    @DisplayName("Negative Register Test: Null Password")
    public void negativeRegister() throws Exception {
        clearAll();
        RegisterRequest registerRequest = new RegisterRequest("user1", null, "email@email.com");
        UserService service = new UserService();
        service.register(registerRequest, authDAO);
        Assertions.assertTrue(UserDAO.userMap.isEmpty());
    }

    @Test
    @DisplayName("Positive Login Test")
    public void positiveLogin() throws Exception {
        clearAll();
        UserDAO.userMap.put("user1", new UserData("user1", "1234", "gmail@gmail.com"));
        LoginRequest loginRequest = new LoginRequest("user1", "1234");
        UserService service = new UserService();
        service.login(loginRequest, authDAO);
        Assertions.assertFalse(AuthDAO.authMap.isEmpty());
    }

    @Test
    @DisplayName("Negative Login Test: Wrong Password")
    public void negativeLogin() throws Exception {
        clearAll();
        UserDAO.userMap.put("user1", new UserData("user1", "1234", "gmail@gmail.com"));
        LoginRequest loginRequest = new LoginRequest("user1", "1235");
        UserService service = new UserService();
        service.login(loginRequest, authDAO);
        Assertions.assertTrue(AuthDAO.authMap.isEmpty());
    }

    @Test
    @DisplayName("Positive Logout Test")
    public void positiveLogout() throws Exception {
        clearAll();
        AuthDAO.authMap.put("1234", new AuthData("1234", "user1"));
        UserService service = new UserService();
        service.logout("1234", authDAO);
        Assertions.assertTrue(AuthDAO.authMap.isEmpty());
    }

    @Test
    @DisplayName("Negative Logout Test: Wrong Token")
    public void negativeLogout() throws Exception {
        clearAll();
        AuthDAO.authMap.put("1234", new AuthData("1234", "user1"));
        UserService service = new UserService();
        service.logout("1235", authDAO);
        Assertions.assertFalse(AuthDAO.authMap.isEmpty());
    }

    @Test
    @DisplayName("Positive Create Game")
    public void positiveCreateGame() throws Exception {
        clearAll();
        AuthDAO.authMap.put("1234", new AuthData("1234", "user1"));
        CreateGameRequest request = new CreateGameRequest("game1", "1234");
        GameService service = new GameService();
        service.createGame(request, authDAO);
        Assertions.assertFalse(GameDAO.gameMap.isEmpty());
    }

    @Test
    @DisplayName("Negative Create Game: Wrong Token")
    public void negativeCreateGame() throws Exception {
        clearAll();
        AuthDAO.authMap.put("1234", new AuthData("1234", "user1"));
        CreateGameRequest request = new CreateGameRequest("game1", "1235");
        GameService service = new GameService();
        service.createGame(request, authDAO);
        Assertions.assertTrue(GameDAO.gameMap.isEmpty());
    }

    @Test
    @DisplayName("Positive Join Game")
    public void positiveJoinGame() throws Exception {
        clearAll();
        AuthDAO.authMap.put("1234", new AuthData("1234", "user1"));
        GameDAO.gameMap.put(1, new GameData(1, "white", null, "game1", new ChessGame()));
        JoinGameRequest request = new JoinGameRequest("1234", "BLACK", 1);
        GameService service = new GameService();
        service.joinGame(request, authDAO);
        Assertions.assertEquals("user1", GameDAO.gameMap.get(1).blackUsername());
    }

    @Test
    @DisplayName("Negative Join Game: Both Colors Taken")
    public void negativeJoinGame() throws Exception {
        clearAll();
        AuthDAO.authMap.put("1234", new AuthData("1234", "user1"));
        GameDAO.gameMap.put(1, new GameData(1, "white", "black", "game1", new ChessGame()));
        JoinGameRequest request = new JoinGameRequest("1234", "BLACK", 1);
        GameService service = new GameService();
        service.joinGame(request, authDAO);
        Assertions.assertSame("black", GameDAO.gameMap.get(1).blackUsername());
    }

    @Test
    @DisplayName("Positive List Games")
    public void positiveListGames() throws Exception {
        clearAll();
        AuthDAO.authMap.put("1234", new AuthData("1234", "user1"));
        ListGamesRequest request = new ListGamesRequest("1234");
        GameService service = new GameService();
        Collection<ListGamesHelperResult> response = service.listGames(request, authDAO);
        ListGamesResult list = new ListGamesResult(response);
        Assertions.assertFalse(list.games().isEmpty());
    }

    @Test
    @DisplayName("Negative List Games: Games is Empty")
    public void negativeListGames() throws Exception {
        clearAll();
        AuthDAO.authMap.put("1234", new AuthData("1234", "user1"));
        ListGamesRequest request = new ListGamesRequest("1235");
        GameService service = new GameService();
        Collection<ListGamesHelperResult> response = service.listGames(request, authDAO);
        ListGamesResult list = new ListGamesResult(response);
        Assertions.assertTrue(list.games().isEmpty());
    }








}
