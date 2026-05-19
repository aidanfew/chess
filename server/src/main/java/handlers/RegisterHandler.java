package handlers;

import com.google.gson.Gson;
import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
import io.javalin.http.Context;
import io.javalin.http.ExceptionHandler;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import requests.RegisterRequest;
import results.RegisterResult;
import service.UserService;

import java.lang.reflect.Type;
import java.util.Objects;

public class RegisterHandler implements Handler {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RegisterHandler that = (RegisterHandler) o;
        return Objects.equals(userService, that.userService);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userService);
    }

    private final UserService userService;
    public RegisterHandler(UserService userService) {
        this.userService = userService;
    }
    public void handle(@NotNull Context ctx) throws Exception {
        var serializer = new Gson();
        String json = ctx.body();
        RegisterRequest request = serializer.fromJson(json, RegisterRequest.class);
        try {
            RegisterResult response = userService.register(request);
            String jsonResponse = serializer.toJson(response);
            ctx.result(jsonResponse);
        } catch (BadRequestException e) {
            var exceptionSerializer = new Gson();
            String exceptionResponse = exceptionSerializer.toJson(e.getMessage());
            ctx.status(e.getStatus());
            ctx.result(exceptionResponse);
        }
    }
}
