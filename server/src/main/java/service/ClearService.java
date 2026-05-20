package service;

import dataaccess.AuthDAO;

public class ClearService {
    public void clear(UserService userService, GameService gameService, AuthDAO authDAO) {
        userService.clear();
        gameService.clear();
        authDAO.clear();
    }
}
