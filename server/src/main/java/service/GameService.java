package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import results.CreateGameResult;
import results.JoinGameResult;

import java.util.Objects;

public class GameService {
    GameDAO game = new GameDAO();

    public CreateGameResult createGame(CreateGameRequest createGameRequest, String authToken, AuthDAO authDataMap) throws Exception {
        String gameName = createGameRequest.gameName();
        if (Objects.equals(gameName, "") || gameName == null) {
            String message = "Error: bad request";
            throw new DataAccessException(message, 400);
        }
        if (SharedServices.userVerified(authToken, authDataMap)) {
            game.createGame(gameName);
            Integer gameID = game.currentGameID();
            return new CreateGameResult(gameID);
        } else if (!SharedServices.userVerified(authToken, authDataMap)) {
            String message = "Error: unauthorized";
            throw new DataAccessException(message, 401);
        } else {
            String message = "Error: (description of error)";
            throw new DataAccessException(message, 500);
        }
    }

    public void joinGame(JoinGameRequest joinGameRequest, String authToken, AuthDAO authDataMap) throws Exception {
        String playerColor = joinGameRequest.playerColor();
        Integer gameID = joinGameRequest.gameID();
        if (game.getGame(gameID) != null && SharedServices.userVerified(authToken, authDataMap)
        && (Objects.equals(playerColor, "WHITE") || Objects.equals(playerColor, "BLACK"))) {
            if (Objects.equals(playerColor, ))
        }
    }

    public void clear() {
        game.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameService that = (GameService) o;
        return Objects.equals(game, that.game);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(game);
    }
}
