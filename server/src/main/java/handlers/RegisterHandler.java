package handlers;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import requests.RegisterRequest;
import service.UserService;

import java.lang.reflect.Type;

public class RegisterHandler implements Handler {
    private final UserService userService;
    public RegisterHandler(UserService userService) {
        this.userService = userService;
    }
    public void handle(@NotNull Context ctx) {
        var serializer = new Gson();
        String json = ctx.body();
        RegisterRequest request = serializer.fromJson(json, RegisterRequest.class);
        userService.register(request);
    }
}
