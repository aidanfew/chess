package dataaccess;

import model.AuthData;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthDAO {
    public static Map<String, AuthData> authMap = new HashMap<>();

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public void createAuth(AuthData authData){
        authMap.put(authData.authToken(), authData);
    }

    public AuthData getAuth(String authToken) {
        return authMap.get(authToken);
    }

    public void deleteAuth(String authToken) {
        authMap.remove(authToken);
    }

    public void clear() {
        authMap.clear();
    }

}
