package server;

import com.google.gson.Gson;
import requests.LoginRequest;import requests.RegisterRequest;import results.LoginResult;import results.LogoutResult;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpResponse;
import exception.ResponseException;import results.RegisterResult;
import java.util.Locale;


public class ServerFacade {
    private static final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

     public LoginResult facadeLogin(String username, String password) throws ResponseException {
        LoginRequest loginRequest = new LoginRequest(username, password);
        Object body = makeRequestBody(loginRequest);
        var request = buildRequest("POST", "/session", body);
        var response = sendRequest(request);
        return handleResponse(response, LoginResult.class);
    }

    public RegisterResult facadeRegister(String username, String password, String email) throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        Object body = makeRequestBody(registerRequest);
        var request = buildRequest("POST", "/user", body);
        var response = sendRequest(request);
        return handleResponse(response, RegisterResult.class);
    }



    private HttpRequest buildRequest(String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type","application/json");
        }
        return request.build();
    }

    private BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw ResponseException.fromJson(body);
            }

            throw new ResponseException(ResponseException.fromHttpStatusCode(status), "other failure: " + status);
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
