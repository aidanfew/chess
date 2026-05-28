package handlers;

import dataaccess.AuthDAO;
import dataaccess.AuthSqlDAO;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.UserService;

import java.util.Objects;

public class LogoutHandler implements Handler {
    private final UserService userService;
    private final AuthSqlDAO authSqlDAO;
    public LogoutHandler(UserService userService, AuthSqlDAO authSqlDAO) {
        this.userService = userService;
        this.authSqlDAO = authSqlDAO;
    }

    public void handle(Context ctx) throws Exception {
        String authToken = ctx.header("authorization");
        try {
            userService.logout(authToken, authSqlDAO);
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
        LogoutHandler that = (LogoutHandler) o;
        return Objects.equals(userService, that.userService) && Objects.equals(authSqlDAO, that.authSqlDAO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userService, authSqlDAO);
    }
}
