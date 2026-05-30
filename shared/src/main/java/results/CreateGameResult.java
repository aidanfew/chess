package results;

import java.util.Objects;

public record CreateGameResult(Integer gameID) {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CreateGameResult that = (CreateGameResult) o;
        return Objects.equals(gameID, that.gameID);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(gameID);
    }
}
