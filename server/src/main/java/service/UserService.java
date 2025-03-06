package service;

import dataaccess.UserDAO;
import dataaccess.AuthDAO;

import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {
    private UserDAO userDAO;
    private AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData register(UserData newUser) {
        // createUser
        userDAO.createUser(newUser);

        // createAuth
        String authToken = UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(authToken, newUser.username());
        authDAO.createAuth(newAuth);

        // return RegisterResult
        return newAuth;
    }

    // public LoginResult login(LoginRequest loginRequest) {}

    // public void logout(LogoutRequest logoutRequest) {}
}
