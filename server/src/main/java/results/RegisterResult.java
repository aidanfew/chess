package results;

import java.util.Objects;

public record RegisterResult(String authToken, String username) {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RegisterResult that = (RegisterResult) o;
        return Objects.equals(username, that.username) && Objects.equals(authToken, that.authToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authToken, username);
    }
}
