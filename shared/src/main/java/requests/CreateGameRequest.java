package requests;

import java.util.Objects;

public record CreateGameRequest(String gameName, String authToken) {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CreateGameRequest that = (CreateGameRequest) o;
        return Objects.equals(gameName, that.gameName) && Objects.equals(authToken, that.authToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameName, authToken);
    }
}
