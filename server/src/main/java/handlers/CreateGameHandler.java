package handlers;

import com.google.gson.Gson;
import dataaccess.AuthSqlDAO;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import requests.CreateGameRequest;
import results.CreateGameResult;
import service.GameService;

import java.util.Objects;

public class CreateGameHandler implements Handler {
    private final GameService gameService;
    private final AuthSqlDAO authSqlDAO;
    public CreateGameHandler(GameService gameService, AuthSqlDAO authSqlDAO) {
        this.gameService = gameService;
        this.authSqlDAO = authSqlDAO;
    }

    public void handle(@NotNull Context ctx) throws Exception {
        var serializer = new Gson();
        String gameName = ctx.body();
        String authToken = ctx.header("authorization");
        CreateGameRequest gameNameRequest = serializer.fromJson(gameName, CreateGameRequest.class);
        CreateGameRequest request = new CreateGameRequest(gameNameRequest.gameName(), authToken);
        try {
            CreateGameResult response = gameService.createGame(request, authSqlDAO);
            String jsonResponse = serializer.toJson(response);
            ctx.result(jsonResponse);
        } catch (DataAccessException e) {
            SharedHandlerMethods.catchException(e, ctx);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CreateGameHandler that = (CreateGameHandler) o;
        return Objects.equals(gameService, that.gameService) && Objects.equals(authSqlDAO, that.authSqlDAO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameService, authSqlDAO);
    }
}
