package handlers;

import com.google.gson.Gson;
import dataaccess.AuthSqlDAO;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import requests.JoinGameRequest;
import service.GameService;

import java.util.Objects;

public class JoinGameHandler implements Handler {
    private final GameService gameService;
    private final AuthSqlDAO authSqlDAO;
    public JoinGameHandler(GameService gameService, AuthSqlDAO authSqlDAO) {
        this.gameService = gameService;
        this.authSqlDAO = authSqlDAO;
    }
    public void handle(Context ctx) throws Exception {
        var serializer = new Gson();
        String gameInfo = ctx.body();
        String authToken = ctx.header("authorization");
        JoinGameRequest gameInfoRequest = serializer.fromJson(gameInfo, JoinGameRequest.class);
        JoinGameRequest request = new JoinGameRequest(authToken, gameInfoRequest.playerColor(), gameInfoRequest.gameID());
        try {
            gameService.joinGame(request, authSqlDAO);
            ctx.result("{}");
        } catch (DataAccessException e) {
            SharedHandlerMethods.catchException(e, ctx);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JoinGameHandler that = (JoinGameHandler) o;
        return Objects.equals(gameService, that.gameService) && Objects.equals(authSqlDAO, that.authSqlDAO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameService, authSqlDAO);
    }
}
