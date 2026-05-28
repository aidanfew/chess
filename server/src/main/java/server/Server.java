package server;

import dataaccess.AuthDAO;
import dataaccess.AuthSqlDAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import handlers.*;
import io.javalin.*;
import service.GameService;
import service.UserService;

import java.util.Objects;

public class Server {

    private final Javalin javalin;

    public Server() {
        AuthSqlDAO authSqlDAO;
        UserService userService;
        GameService gameService;
        try {
            authSqlDAO = new AuthSqlDAO();
            userService = new UserService();
            gameService = new GameService();
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
            javalin = Javalin.create(config -> config.staticFiles.add("web"))
                    .delete("/db", new ClearHandler(userService, gameService, authSqlDAO))
                    .post("/user", new RegisterHandler(userService, authSqlDAO))
                    .post("/session", new LoginHandler(userService, authSqlDAO))
                    .delete("/session", new LogoutHandler(userService, authSqlDAO))
                    .post("/game", new CreateGameHandler(gameService, authSqlDAO))
                    .put("/game", new JoinGameHandler(gameService, authSqlDAO))
                    .get("/game", new ListGamesHandler(gameService, authSqlDAO));
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Server server = (Server) o;
        return Objects.equals(javalin, server.javalin);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(javalin);
    }
}
