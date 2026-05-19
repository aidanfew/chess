package dataaccess;

import model.AuthData;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class AuthDAO {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthDAO authDAO = (AuthDAO) o;
        return Objects.equals(AuthMap, authDAO.AuthMap);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(AuthMap);
    }

    Map<String, String> AuthMap = new HashMap<>();

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public AuthData createAuth(String username){
        return new AuthData(generateToken(), username);
    }

    public void storeAuth(AuthData authData) {
        AuthMap.put(authData.username(), authData.authToken());
    }

    public String getAuth(String username) {
        return AuthMap.getOrDefault(username, null);
    }


}
