package handlers;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import requests.RegisterRequest;
import results.RegisterResult;
import service.UserService;

import java.util.Objects;

public class RegisterHandler implements Handler {
    private final UserService userService;
    private final AuthDAO authDAO;
    public RegisterHandler(UserService userService, AuthDAO authDAO) {
        this.userService = userService;
        this.authDAO = authDAO;
    }
    public void handle(@NotNull Context ctx) throws Exception {
        var serializer = new Gson();
        String json = ctx.body();
        RegisterRequest request = serializer.fromJson(json, RegisterRequest.class);
        try {
            RegisterResult response = userService.register(request, authDAO);
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
        RegisterHandler that = (RegisterHandler) o;
        return Objects.equals(userService, that.userService) && Objects.equals(authDAO, that.authDAO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userService, authDAO);
    }
}
