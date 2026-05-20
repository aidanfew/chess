package service;

import dataaccess.GameDAO;

import java.util.Objects;

public class GameService {
    GameDAO game = new GameDAO();

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
