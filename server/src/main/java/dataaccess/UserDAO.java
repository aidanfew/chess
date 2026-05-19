package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class UserDAO {
    Map<String, UserData> UserMap = new HashMap<>();

    public void createUser(UserData userData) {
        UserMap.put(userData.username(), userData);
    }

    public UserData getUser(String username) {
        return UserMap.getOrDefault(username, null);
    }


}
