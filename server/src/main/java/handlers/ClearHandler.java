package handlers;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class ClearHandler implements Handler {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    public ClearHandler(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public void handle(Context ctx) throws Exception {
        try {
            userDAO.clear();
            authDAO.clear();
            gameDAO.clear();
            ctx.result("{}");
        } catch (DataAccessException e){

        }
    }
}
