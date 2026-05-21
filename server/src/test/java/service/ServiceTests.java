package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requests.LoginRequest;
import requests.RegisterRequest;
import results.RegisterResult;


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
        Assertions.assertTrue(AuthDAO.AuthMap.isEmpty());
        Assertions.assertTrue(UserDAO.UserMap.isEmpty());
        Assertions.assertTrue(GameDAO.GameMap.isEmpty());
    }

    @Test
    @DisplayName("Positive Register Test")
    public void positiveRegister() throws Exception {
        clearAll();
        RegisterRequest registerRequest = new RegisterRequest("user1", "pass1", "email@email.com");
        UserService service = new UserService();
        service.register(registerRequest, authDAO);
        Assertions.assertFalse(UserDAO.UserMap.isEmpty());
    }

    @Test
    @DisplayName("Negative Register Test: Null Password")
    public void negativeRegister() throws Exception {
        clearAll();
        RegisterRequest registerRequest = new RegisterRequest("user1", null, "email@email.com");
        UserService service = new UserService();
        service.register(registerRequest, authDAO);
        Assertions.assertTrue(UserDAO.UserMap.isEmpty());
    }

    @Test
    @DisplayName("Positive Login Test")
    public void positiveLogin() throws Exception {
        clearAll();
        UserDAO.UserMap.put("user1", new UserData("user1", "1234", "gmail@gmail.com"));
        LoginRequest loginRequest = new LoginRequest("user1", "1234");
        UserService service = new UserService();
        service.login(loginRequest, authDAO);
        Assertions.assertFalse(AuthDAO.AuthMap.isEmpty());
    }

    @Test
    @DisplayName("Negative Login Test: Wrong Password")
    public void negativeLogin() throws Exception {
        clearAll();
        UserDAO.UserMap.put("user1", new UserData("user1", "1234", "gmail@gmail.com"));
        LoginRequest loginRequest = new LoginRequest("user1", "1235");
        UserService service = new UserService();
        service.login(loginRequest, authDAO);
        Assertions.assertTrue(AuthDAO.AuthMap.isEmpty());
    }






}
