package service;

import model.UserData;

public class SharedServices {
    public static boolean passwordCorrect(String password, UserData userData){
        return userData.password().equals(password);
    }
}
