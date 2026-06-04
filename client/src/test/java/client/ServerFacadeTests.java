package client;

import dataaccess.AuthSqlDAO;
import dataaccess.DataAccessException;
import exception.ResponseException;
import model.AuthData;
import org.junit.jupiter.api.*;
import results.JoinGameResult;
import server.Server;
import server.ServerFacade;
import service.ClearService;
import service.GameService;
import service.UserService;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    UserService user = new UserService();
    GameService game = new GameService();
    AuthSqlDAO auth = new AuthSqlDAO();

    public ServerFacadeTests() throws DataAccessException {

    }

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void clearAll() throws Exception {
        ClearService clearService = new ClearService();
        clearService.clear(user, game, auth);
    }

    @Test
    @DisplayName("Register Positive")
    public void registerPositive() throws Exception {
        var authData = facade.facadeRegister("player1", "password", "p1@email.com");
        Assertions.assertTrue(authData.authToken().length() > 10);
    }

    @Test
    @DisplayName("Register Negative")
    public void registerNegative() {
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.facadeRegister("user1", null, "donkeykong");
        });
    }

    @Test
    @DisplayName("Login Positive")
    public void loginPositive() throws Exception {
        facade.facadeRegister("user1", "1234", "donkeykong");
        var authData = facade.facadeLogin("user1", "1234");
        Assertions.assertTrue(authData.authToken().length() > 10);
    }

    @Test
    @DisplayName("Login Negative")
    public void loginNegative()  {
        Assertions.assertThrows(ResponseException.class, () -> {
           facade.facadeLogin("user1", "1234");
        });
    }

    @Test
    @DisplayName("Logout Positive")
    public void logoutPositive() throws Exception {
        var authData = facade.facadeRegister("user1", "1234", "user@user");
        facade.facadeLogout(authData.authToken());
        Assertions.assertNull(auth.getAuth(authData.authToken()));
    }

    @Test
    @DisplayName("Logout Negative")
    public void logoutNegative() throws Exception {
        var authData = facade.facadeRegister("user2", "1234", "diddykong");
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.facadeLogout("1234");
        });
    }

    @Test
    @DisplayName("Create Game Positive")
    public void createGamePositive() throws Exception {
        var authData = facade.facadeRegister("user1", "1234", "diddykong");
        var gameData = facade.facadeCreateGame("gamename", authData.authToken());
        Assertions.assertNotNull(gameData.gameID());
    }

    @Test
    @DisplayName("Create Game Negative")
    public void createGameNegative() throws Exception {
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.facadeCreateGame("game1", "1234");
        });
    }

    @Test
    @DisplayName("List Games Positive")
    public void listGamesPositive() throws Exception {
        var authData = facade.facadeRegister("mario", "1234", "bowser");
        var gameData = facade.facadeCreateGame("game1", authData.authToken());
        Assertions.assertNotNull(facade.facadeListGames(authData.authToken()));
    }

    @Test
    @DisplayName("List Games Negative")
    public void listGamesNegative() throws Exception {
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.facadeListGames("1234");
        });
    }

    @Test
    @DisplayName("Join Game Positive")
    public void joinGamePositive() throws Exception {
        var authData = facade.facadeRegister("user3", "1234", "use3");
        var gameData = facade.facadeCreateGame("gamename", authData.authToken());
        var result = facade.facadeJoinGame(authData.authToken(), "WHITE", gameData.gameID());
        Assertions.assertEquals(JoinGameResult.class, result.getClass());
    }

    @Test
    @DisplayName("Join Game Negative")
    public void joinGameNegative() throws Exception {
        var authData = facade.facadeRegister("user4", "1234", "user4");
        var gameData = facade.facadeCreateGame("game1", authData.authToken());
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.facadeJoinGame(authData.authToken(), "BLACK", 2);
        });
    }
}
