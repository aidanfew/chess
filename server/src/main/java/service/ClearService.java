package service;

import dataaccess.AuthDAO;
import dataaccess.AuthSqlDAO;

public class ClearService {
    public void clear(UserService userService, GameService gameService, AuthSqlDAO authSqlDAO) throws Exception {
        userService.clear();
        gameService.clear();
        authSqlDAO.clear();
    }
}
