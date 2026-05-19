package service;

import dataaccess.AlreadyTakenException;
import dataaccess.AuthDAO;
import dataaccess.BadRequestException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import requests.RegisterRequest;
import results.RegisterResult;

import java.util.Objects;

public class UserService {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserService that = (UserService) o;
        return Objects.equals(user, that.user) && Objects.equals(auth, that.auth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, auth);
    }

    UserDAO user = new UserDAO();
    AuthDAO auth = new AuthDAO();
    public RegisterResult register(RegisterRequest registerRequest) throws Exception {
        String username = registerRequest.username();
        String password = registerRequest.password();
        String email = registerRequest.email();
        if (Objects.equals(username, "") || Objects.equals(password, "") || Objects.equals(email, "")) {
            String message = "Error: bad request";
            throw new BadRequestException(message, 400);
        }
        if (user.getUser(username) == null){
            user.createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
            AuthData authData = auth.createAuth(registerRequest.username());
            auth.storeAuth(authData);
            return new RegisterResult(authData.authToken(), username);
        } else {
            String message = "Error: already taken";
            throw new BadRequestException(message, 403);
        }
    }
}
