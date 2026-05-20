package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;

import java.util.Map;

public class SharedHandlerMethods {
    public static void catchException(DataAccessException e, Context ctx) {
        var exceptionSerializer = new Gson();
        Map<String, String> map = Map.of("message", e.getMessage());
        String exceptionResponse = exceptionSerializer.toJson(map);
        ctx.status(e.getStatus());
        ctx.result(exceptionResponse);
    }

//    public void serializeRequest(Context ctx, Class<?> type) {
//        var serializer = new Gson();
//        String json = ctx.body();
//        type request = serializer.fromJson(json, type.getDeclaringClass());
//    }
}
