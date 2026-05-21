package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserDAO {
    public static Map<String, UserData> UserMap = new HashMap<>();

    public void createUser(UserData userData) {
        UserMap.put(userData.username(), userData);
    }

    public UserData getUser(String username) {
        return UserMap.getOrDefault(username, null);
    }

    public void clear(){
        UserMap.clear();
    }

}
