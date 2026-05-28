package handlers;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.AuthSqlDAO;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import requests.LoginRequest;
import results.LoginResult;
import service.UserService;

import java.util.Objects;

public class LoginHandler implements Handler {
    private final UserService userService;
    private final AuthSqlDAO authSqlDAO;
    public LoginHandler(UserService userService, AuthSqlDAO authSqlDAO) {
        this.userService = userService;
        this.authSqlDAO = authSqlDAO;
    }
    public void handle(Context ctx) throws Exception {
        var serializer = new Gson();
        String json = ctx.body();
        LoginRequest request = serializer.fromJson(json, LoginRequest.class);
        try {
            LoginResult response = userService.login(request, authSqlDAO);
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
        return Objects.equals(userService, that.userService) && Objects.equals(authSqlDAO, that.authSqlDAO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userService, authSqlDAO);
    }
}
