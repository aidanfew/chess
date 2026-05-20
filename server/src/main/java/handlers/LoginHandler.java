package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import requests.LoginRequest;
import results.LoginResult;
import service.UserService;

import java.util.Objects;

public class LoginHandler implements Handler {
    private final UserService userService;
    public LoginHandler(UserService userService) {
        this.userService = userService;
    }
    public void handle(Context ctx) throws Exception {
        var serializer = new Gson();
        String json = ctx.body();
        LoginRequest request = serializer.fromJson(json, LoginRequest.class);
        try {
            LoginResult response = userService.login(request);
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
        LoginHandler that = (LoginHandler) o;
        return Objects.equals(userService, that.userService);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userService);
    }
}
