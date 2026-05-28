package service;

import dataaccess.AuthSqlDAO;
import model.UserData;

public class SharedServices {
    public static boolean passwordCorrect(String password, UserData userData) {
        return userData.password().equals(password);
    }

    public static boolean userVerified(String authToken, AuthSqlDAO authDAO) throws Exception {
        return authDAO.getAuth(authToken) != null;
    }

//    public static boolean playerColorVerified(String playerColor) {
//        if (!Objects.equals(playerColor, "WHITE") || !Objects.equals(playerColor, "BLACK"))
//    }
}