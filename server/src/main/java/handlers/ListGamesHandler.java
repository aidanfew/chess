package handlers;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.AuthSqlDAO;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import requests.ListGamesRequest;
import results.ListGamesHelperResult;
import results.ListGamesResult;
import service.GameService;

import java.util.Collection;
import java.util.Objects;

public class ListGamesHandler implements Handler {
    private final GameService gameService;
    private final AuthSqlDAO authSqlDAO;

    public ListGamesHandler(GameService gameService, AuthSqlDAO authSqlDAO) {
        this.gameService = gameService;
        this.authSqlDAO = authSqlDAO;
    }
    public void handle(Context ctx) throws Exception {
        var serializer = new Gson();
        String authToken = ctx.header("authorization");
        ListGamesRequest request = new ListGamesRequest(authToken);
        try {
            Collection<ListGamesHelperResult> response = gameService.listGames(request, authSqlDAO);
            ListGamesResult list = new ListGamesResult(response);
            ctx.result(serializer.toJson(list));
        } catch (DataAccessException e) {
            SharedHandlerMethods.catchException(e, ctx);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ListGamesHandler that = (ListGamesHandler) o;
        return Objects.equals(gameService, that.gameService) && Objects.equals(authSqlDAO, that.authSqlDAO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameService, authSqlDAO);
    }
}

