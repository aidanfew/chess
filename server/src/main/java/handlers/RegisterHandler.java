package handlers;

import com.google.gson.Gson;
import dataaccess.AlreadyTakenException;
import io.javalin.http.Context;
import io.javalin.http.ExceptionHandler;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import requests.RegisterRequest;
import results.RegisterResult;
import service.UserService;

import java.lang.reflect.Type;

public class RegisterHandler implements Handler {
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
        } catch (AlreadyTakenException e) {


        }
    }
}
