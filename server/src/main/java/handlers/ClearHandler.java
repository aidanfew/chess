package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.ClearService;
import service.GameService;
import service.UserService;

import java.util.Objects;

public class ClearHandler implements Handler {
    private final UserService userService;
    private final GameService gameService;
    public ClearHandler(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    public void handle(Context ctx) throws Exception {
        try {
            ClearService fullClear = new ClearService();
            fullClear.clear(userService, gameService);
            ctx.result("{}");
        } catch(DataAccessException e) {
            SharedHandlerMethods.catchException(e, ctx);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClearHandler that = (ClearHandler) o;
        return Objects.equals(userService, that.userService) && Objects.equals(gameService, that.gameService);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userService, gameService);
    }
}
