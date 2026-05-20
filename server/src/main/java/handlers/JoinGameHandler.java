package handlers;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.eclipse.jetty.server.Authentication;
import requests.JoinGameRequest;
import service.GameService;
import service.UserService;

public class JoinGameHandler implements Handler {
    private final UserService userService;
    private final GameService gameService;
    public JoinGameHandler(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }
    public void handle(Context ctx) throws Exception {
        var serializer = new Gson();
        String gameInfo = ctx.body();
        String authToken = ctx.header("authorization");
        JoinGameRequest request = serializer.fromJson(gameInfo, JoinGameRequest.class);
        try {
            gameService.
            ctx.result("{}");
        }
    }


}
