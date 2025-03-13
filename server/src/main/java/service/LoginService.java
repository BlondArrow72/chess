package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
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

    public AuthData login(LoginRequest loginRequest) throws UnauthorizedUserError, DataAccessException {
        // get user
        UserData existingUser = null;
        try {
            existingUser = userDAO.getUser(loginRequest.username());
        } catch (dataaccess.DataAccessException e) {
            throw new RuntimeException(e);
        }
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
