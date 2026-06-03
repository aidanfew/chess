package client;

import dataaccess.AuthSqlDAO;
import dataaccess.DataAccessException;
import exception.ResponseException;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import service.ClearService;
import service.GameService;
import service.UserService;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class ServerFacadeTests {

    private static Server server = new Server();
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
        facade.facadeRegister("user1", "1234", "user@user");

        Assertions.assertDoesNotThrow(() -> {

        });
    }
}
