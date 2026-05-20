package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserDAO {
    Map<String, UserData> UserMap = new HashMap<>();

    public void createUser(UserData userData) {
        UserMap.put(userData.username(), userData);
    }

    public UserData getUser(String username) {
        return UserMap.getOrDefault(username, null);
    }

    public void clear(){
        UserMap.clear();
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDAO userDAO = (UserDAO) o;
        return Objects.equals(UserMap, userDAO.UserMap);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(UserMap);
    }
}
