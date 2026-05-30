package results;

import java.util.Collection;
import java.util.Objects;

public record ListGamesResult(Collection<ListGamesHelperResult> games) {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ListGamesResult that = (ListGamesResult) o;
        return Objects.equals(games, that.games);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(games);
    }
}
