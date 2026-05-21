package dataaccess;

import chess.ChessGame;
import model.GameData;
import results.ListGamesHelperResult;

import java.util.*;

public class GameDAO {
    public static Map<Integer, GameData> gameMap = new HashMap<>();
    Integer gameID = 0;

    public void createGame(String gameName) {
        gameID++;
        GameData gameData = new GameData(gameID, null, null,
                gameName, new ChessGame());
        gameMap.put(gameID, gameData);
    }

    public Integer currentGameID() {
        return gameID;
    }

    public GameData getGame(int gameID) {
        return gameMap.get(gameID);
    }

    public Collection<GameData> listGames(HashMap<Integer, GameData> gameMap) {
        return new ArrayList<>(gameMap.values());
    }

    public void replaceGame(int gameID, GameData oldData, GameData newData) {
        gameMap.replace(gameID, oldData, newData);
    }

    public void updateGame(int gameID, ChessGame newGame) {
        GameData oldData = gameMap.get(gameID);
        GameData newData = new GameData(oldData.gameID(),
                oldData.whiteUsername(),
                oldData.blackUsername(),
                oldData.gameName(),
                newGame);
        gameMap.replace(gameID, oldData, newData);
    }

    public Collection<ListGamesHelperResult> createHelperList() {
        ArrayList<ListGamesHelperResult> list = new ArrayList<>();
        for (GameData game : gameMap.values()) {
            ListGamesHelperResult result = new ListGamesHelperResult(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName());
            list.add(result);
        }
        return list;
    }

    public void clear() {
        gameMap.clear();
    }
}
