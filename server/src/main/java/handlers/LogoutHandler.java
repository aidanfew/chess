package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import requests.LogoutRequest;
import results.LogoutResult;
import service.UserService;

import java.util.Objects;

public class LogoutHandler implements Handler {
    private final UserService userService;
    public LogoutHandler(UserService userService) {
        this.userService = userService;
    }
    public void handle(Context ctx) throws Exception {
        String authToken = ctx.header("authorization");
        try {
            userService.logout(authToken);
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
        return Objects.equals(userService, that.userService);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userService);
    }
}
