package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;

import handlers.LoginRequest;

import model.UserData;
import model.AuthData;

public class LoginService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public LoginService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData login(LoginRequest loginRequest) throws UnauthorizedUserError {
        // get user
        UserData existingUser = userDAO.getUser(loginRequest.username());
        if (existingUser == null) {
            throw new UnauthorizedUserError();
        }

        // verify passwords match
        if (!loginRequest.password().equals(existingUser.password())) {
            throw new UnauthorizedUserError();
        }

        // create new auth
        return authDAO.createAuth(loginRequest.username());
    }
}
