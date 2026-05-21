package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class UserDAO {
    public static Map<String, UserData> userMap = new HashMap<>();

    public void createUser(UserData userData) {
        userMap.put(userData.username(), userData);
    }

    public UserData getUser(String username) {
        return userMap.getOrDefault(username, null);
    }

    public void clear(){
        userMap.clear();
    }

}
