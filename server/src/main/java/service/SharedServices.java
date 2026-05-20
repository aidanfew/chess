package service;

import dataaccess.AuthDAO;
import model.UserData;

public class SharedServices {
    public static boolean passwordCorrect(String password, UserData userData) {
        return userData.password().equals(password);
    }

    public static boolean userVerified(String authToken, AuthDAO authDataMap) {
        return authDataMap.getAuth(authToken) != null;
    }
}