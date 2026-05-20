package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import requests.ListGamesRequest;
import results.CreateGameResult;
import results.JoinGameResult;
import results.ListGamesHelperResult;
import results.ListGamesResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class GameService {
    GameDAO game = new GameDAO();

    public CreateGameResult createGame(CreateGameRequest createGameRequest, AuthDAO authDAO) throws Exception {
        String gameName = createGameRequest.gameName();
        String authToken = createGameRequest.authToken();
        if (Objects.equals(gameName, "") || gameName == null || Objects.equals(authToken, "") || authToken == null) {
            String message = "Error: bad request";
            throw new DataAccessException(message, 400);
        }
        if (SharedServices.userVerified(authToken, authDAO)) {
            game.createGame(gameName);
            Integer gameID = game.currentGameID();
            return new CreateGameResult(gameID);
        } else if (!SharedServices.userVerified(authToken, authDAO)) {
            String message = "Error: unauthorized";
            throw new DataAccessException(message, 401);
        } else {
            String message = "Error: (description of error)";
            throw new DataAccessException(message, 500);
        }
    }

    public void joinGame(JoinGameRequest joinGameRequest, AuthDAO authDAO) throws Exception {
        String playerColor = joinGameRequest.playerColor();
        String authToken = joinGameRequest.authToken();
        Integer gameID = joinGameRequest.gameID();
        if (gameID == null) {
            String message = "Error: bad request";
            throw new DataAccessException(message, 400);
        }
        GameData gameData = game.getGame(gameID);
        if (gameData == null) {
            String message = "Error: unauthorized";
            throw new DataAccessException(message, 401);
        }
        if (!SharedServices.userVerified(authToken, authDAO)) {
            String message = "Error: unauthorized";
            throw new DataAccessException(message, 401);
        }
        if (!Objects.equals(playerColor, "WHITE") && !Objects.equals(playerColor, "BLACK")) {
            String message = "Error: bad request";
            throw new DataAccessException(message, 400);
        }
        if (Objects.equals(playerColor, "BLACK") && gameData.blackUsername() == null) {
            String newBlackUser = authDAO.getAuth(authToken).username();
            GameData joined = new GameData(gameData.gameID(), gameData.whiteUsername(), newBlackUser, gameData.gameName(), gameData.game());
            game.replaceGame(gameID, gameData, joined);
        } else if (Objects.equals(playerColor, "WHITE") && gameData.whiteUsername() == null) {
            String newWhiteUser = authDAO.getAuth(authToken).username();
            GameData joined = new GameData(gameData.gameID(), newWhiteUser, gameData.blackUsername(), gameData.gameName(), gameData.game());
            game.replaceGame(gameID, gameData, joined);
        } else {
            String message = "Error: already taken";
            throw new DataAccessException(message, 403);
        }
    }

    public Collection<ListGamesHelperResult> listGames(ListGamesRequest listGamesRequest, AuthDAO authDAO) throws Exception {
        if (!SharedServices.userVerified(listGamesRequest.authToken(), authDAO)) {
            String message = "Error: unauthorized";
            throw new DataAccessException(message, 401);
        }
        return game.createHelperList();
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
