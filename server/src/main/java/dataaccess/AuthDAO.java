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

    public void createAuth(AuthData authData){
        AuthMap.put(authData.username(), generateToken());
    }

    public String getAuth(String username) {
        return AuthMap.getOrDefault(username, null);
    }


}
