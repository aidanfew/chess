package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import requests.CreateGameRequest;
import results.CreateGameResult;
import service.GameService;
import service.UserService;

import java.util.Objects;

public class CreateGameHandler implements Handler {
    private final UserService userService;
    private final GameService gameService;
    public CreateGameHandler(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }
    public void handle(@NotNull Context ctx) throws Exception {
        var serializer = new Gson();
        String gameName = ctx.body();
        String authToken = ctx.header("authorization");
        CreateGameRequest request = serializer.fromJson(gameName, CreateGameRequest.class);
        try {
            CreateGameResult response = gameService.createGame(request, authToken, userService.provideAuthData());
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
        return Objects.equals(userService, that.userService) && Objects.equals(gameService, that.gameService);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userService, gameService);
    }
}
