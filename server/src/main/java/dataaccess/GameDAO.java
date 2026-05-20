package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.*;

public class GameDAO {
    Map<Integer, GameData> GameMap = new HashMap<>();

    public void createGame(GameData gameData) {
        GameMap.put(gameData.gameID(), gameData);
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
