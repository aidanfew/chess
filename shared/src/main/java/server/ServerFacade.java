package server;

import com.google.gson.Gson;
import requests.*;
import results.*;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpResponse;
import exception.ResponseException;

import java.util.Objects;


public class ServerFacade {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

     public LoginResult facadeLogin(String username, String password) throws ResponseException {
        LoginRequest loginRequest = new LoginRequest(username, password);
        var request = buildRequest("POST", "/session", loginRequest);
        var response = sendRequest(request);
        return handleResponse(response, LoginResult.class);
    }

    public RegisterResult facadeRegister(String username, String password, String email) throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        var request = buildRequest("POST", "/user", registerRequest);
        var response = sendRequest(request);
        return handleResponse(response, RegisterResult.class);
    }

    public LogoutResult facadeLogout(String authToken) throws ResponseException {
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        var request = buildRequest("DELETE", "/session", logoutRequest);
        var response = sendRequest(request);
        return handleResponse(response, LogoutResult.class);
    }

    public CreateGameResult facadeCreateGame(String gameName, String authToken) throws ResponseException{
        CreateGameRequest createGameRequest = new CreateGameRequest(gameName,authToken);
        var request = buildRequest("POST", "/game", createGameRequest);
        var response = sendRequest(request);
        return handleResponse(response, CreateGameResult.class);
    }

    public ListGamesResult facadeListGames(String authToken) throws ResponseException {
        ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);
        var request = buildRequest("GET", "/game", listGamesRequest);
        var response = sendRequest(request);
        return handleResponse(response, ListGamesResult.class);
    }

    public JoinGameResult facadeJoinGame(String authToken, String playerColor, int gameID) throws ResponseException {
        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, playerColor, gameID);
        var request = buildRequest("PUT", "/game", joinGameRequest);
        var response = sendRequest(request);
        return handleResponse(response, JoinGameResult.class);
    }



    private HttpRequest buildRequest(String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            if (Objects.equals(body.getClass(), LogoutRequest.class)) {
                request.setHeader("authorization", ((LogoutRequest) body).authToken());
            } else if (Objects.equals(body.getClass(), ListGamesRequest.class)) {
                request.setHeader("authorization", ((ListGamesRequest) body).authToken());
            } else if (Objects.equals(body.getClass(), CreateGameRequest.class)) {
                request.setHeader("authorization", ((CreateGameRequest) body).authToken());
            } else if (Objects.equals(body.getClass(), JoinGameRequest.class)) {
                request.setHeader("authorization", ((JoinGameRequest) body).authToken());
            }
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
            return CLIENT.send(request, BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException(ResponseException.Code.ServerError, "other failure");
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
