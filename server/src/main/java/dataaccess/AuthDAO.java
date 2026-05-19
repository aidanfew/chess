package dataaccess;

import model.AuthData;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthDAO {
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
