package service;

import com.google.gson.JsonArray;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import requests.LoginRequest;
import requests.LogoutRequest;
import requests.RegisterRequest;
import results.LoginResult;
import results.LogoutResult;
import results.RegisterResult;

import java.util.Objects;

public class UserService {
    UserDAO user = new UserDAO();


    public RegisterResult register(RegisterRequest registerRequest, AuthDAO authDAO) throws Exception {
        String username = registerRequest.username();
        String password = registerRequest.password();
        String email = registerRequest.email();
        if (Objects.equals(username, "") || Objects.equals(password, "") || Objects.equals(email, "")
        || username == null || password == null || email == null) {
            String message = "Error: bad request";
            throw new DataAccessException(message, 400);
        }
        if (user.getUser(username) == null){
            user.createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
            String newToken = AuthDAO.generateToken();
            authDAO.createAuth(new AuthData(newToken, username));
            return new RegisterResult(newToken, username);
        } else if (user.getUser(username) != null) {
            String message = "Error: already taken";
            throw new DataAccessException(message, 403);
        } else {
            String message = "Error: (description of error)";
            throw new DataAccessException(message, 500);
        }
    }


    public LoginResult login(LoginRequest loginRequest, AuthDAO authDAO) throws Exception {
        String username = loginRequest.username();
        String password = loginRequest.password();
        if (Objects.equals(username, "") || Objects.equals(password, "") || username == null || password == null) {
            String message = "Error: bad request";
            throw new DataAccessException(message, 400);
        }
        if (user.getUser(username) != null && SharedServices.passwordCorrect(password, user.getUser(username))) {
            String newToken = AuthDAO.generateToken();
            authDAO.createAuth(new AuthData(newToken, username));
            return new LoginResult(newToken, username);
        } else if (user.getUser(username) == null || !SharedServices.passwordCorrect(password, user.getUser(username))) {
            String message = "Error: unauthorized";
            throw new DataAccessException(message, 401);
        } else {
            String message = "Error: (description of error)";
            throw new DataAccessException(message, 500);
        }
    }


    public LogoutResult logout(String authToken, AuthDAO authDAO) throws Exception {
        if (authDAO.getAuth(authToken) != null) {
            authDAO.deleteAuth(authToken);
            return new LogoutResult();
        } else {
            String message = "Error: unauthorized";
            throw new DataAccessException(message, 401);
        }
    }

    public void clear() {
        user.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserService that = (UserService) o;
        return Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(user);
    }
}
