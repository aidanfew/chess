package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.*;

public class GameDAO {
    Map<Integer, GameData> GameMap = new HashMap<>();
    Integer gameID = 0;

    public void createGame(String gameName) {
        gameID++;
        GameData gameData = new GameData(gameID, null, null,
                gameName, new ChessGame());
        GameMap.put(gameID, gameData);
    }

    public Integer currentGameID() {
        return gameID;
    }

    public GameData getGame(int gameID) {
        return GameMap.get(gameID);
    }

    public Collection<GameData> listGames(HashMap<Integer, GameData> gameMap) {
        return new ArrayList<>(gameMap.values());
    }

    public void updateGame(int gameID, ChessGame newGame) {
        GameData oldData = GameMap.get(gameID);
        GameData newData = new GameData(oldData.gameID(),
                oldData.whiteUsername(),
                oldData.blackUsername(),
                oldData.gameName(),
                newGame);
        GameMap.replace(gameID, oldData, newData);
    }

    public void joinGame()

    public void clear() {
        GameMap.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameDAO gameDAO = (GameDAO) o;
        return Objects.equals(GameMap, gameDAO.GameMap);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(GameMap);
    }
}
