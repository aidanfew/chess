package service;

import dataaccess.AuthDAO;
import model.UserData;

import java.util.Objects;

public class SharedServices {
    public static boolean passwordCorrect(String password, UserData userData) {
        return userData.password().equals(password);
    }

    public static boolean userVerified(String authToken, AuthDAO authDataMap) {
        return authDataMap.getAuth(authToken) != null;
    }

    public static boolean playerColorVerified(String playerColor) {
        if (!Objects.equals(playerColor, "WHITE") || !Objects.equals(playerColor, "BLACK"))
    }
}