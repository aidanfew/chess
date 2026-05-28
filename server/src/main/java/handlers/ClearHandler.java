package handlers;

import dataaccess.AuthDAO;
import dataaccess.AuthSqlDAO;
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
    private final AuthSqlDAO authSqlDAO;
    public ClearHandler(UserService userService, GameService gameService, AuthSqlDAO authSqlDAO) {
        this.userService = userService;
        this.gameService = gameService;
        this.authSqlDAO = authSqlDAO;
    }

    public void handle(Context ctx) throws Exception {
        try {
            ClearService fullClear = new ClearService();
            fullClear.clear(userService, gameService, authSqlDAO);
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
