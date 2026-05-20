package dataaccess;

import model.AuthData;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class AuthDAO {
    Map<String, AuthData> AuthMap = new HashMap<>();

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public void createAuth(AuthData authData){
        AuthMap.put(authData.authToken(), authData);
    }

    public AuthData getAuth(String authToken) {
        return AuthMap.get(authToken);
    }

    public void deleteAuth(String authToken) {
        AuthMap.remove(authToken);
    }

    public void clear() {
        AuthMap.clear();
    }


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

}
