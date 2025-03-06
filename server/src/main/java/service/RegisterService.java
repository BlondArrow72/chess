package service;

import dataaccess.UserDAO;
import dataaccess.AuthDAO;

import model.AuthData;
import model.UserData;

import java.util.UUID;

public class RegisterService {
    private UserDAO userDAO;
    private AuthDAO authDAO;

    public RegisterService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData register(UserData newUser) throws UserTakenException {
        // check if username is already taken
        if (userDAO.getUser(newUser.username()) == null) {
            // createUser
            userDAO.createUser(newUser);
        }
        else {
            throw new UserTakenException("Error: already taken");
        }

        // createAuth
        String authToken = UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(authToken, newUser.username());
        authDAO.createAuth(newAuth);

        // return RegisterResult
        return newAuth;
    }
}
