package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;

public class SharedHandlerMethods {
    public static void catchException(DataAccessException e, Context ctx) {
        var exceptionSerializer = new Gson();
        String exceptionResponse = exceptionSerializer.toJson(e.getMessage());
        ctx.status(e.getStatus());
        ctx.result(exceptionResponse);
    }
}
