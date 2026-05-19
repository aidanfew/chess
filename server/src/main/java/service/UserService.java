package service;

import dataaccess.AlreadyTakenException;
import dataaccess.AuthDAO;
import dataaccess.BadRequestException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import requests.RegisterRequest;
import results.RegisterResult;

public class UserService {
    UserDAO user = new UserDAO();
    AuthDAO auth = new AuthDAO();
    public RegisterResult register(RegisterRequest registerRequest) throws Exception {
        String username = registerRequest.username();
        String password = registerRequest.password();
        String email = registerRequest.email();
        if (username == null || password == null || email == null) {
            String message = "Error: bad request";
            throw new BadRequestException(message);
        }
        if (user.getUser(username) == null){
            user.createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
            AuthData authData = auth.createAuth(registerRequest.username());
            auth.storeAuth(authData);
            return new RegisterResult(authData.authToken(), username);
        } else {
            String message = "Error: already taken";
            throw new AlreadyTakenException(message);
        }
    }
}
