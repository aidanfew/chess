package requests;

import java.util.Objects;

public record JoinGameRequest(String authToken, String playerColor, Integer gameID) {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JoinGameRequest that = (JoinGameRequest) o;
        return Objects.equals(gameID, that.gameID) && Objects.equals(authToken, that.authToken) && Objects.equals(playerColor, that.playerColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authToken, playerColor, gameID);
    }
}
