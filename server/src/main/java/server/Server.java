package server;

import dataaccess.AuthDAO;
import handlers.*;
import io.javalin.*;
import service.GameService;
import service.UserService;

import java.util.Objects;

public class Server {

    private final Javalin javalin;

    public Server() {
        UserService userService = new UserService();
        GameService gameService = new GameService();
        AuthDAO authDAO = new AuthDAO();
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .delete("/db", new ClearHandler(userService, gameService, authDAO))
                .post("/user", new RegisterHandler(userService, authDAO))
                .post("/session", new LoginHandler(userService, authDAO))
                .delete("/session", new LogoutHandler(userService, authDAO))
                .post("/game", new CreateGameHandler(gameService, authDAO))
                .put("/game", new JoinGameHandler(gameService, authDAO))
                .get("/game", new ListGamesHandler(gameService, authDAO));
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
