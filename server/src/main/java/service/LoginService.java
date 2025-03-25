package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;

import model.LoginRequest;

import model.UserData;
import model.AuthData;

import org.mindrot.jbcrypt.BCrypt;

public class LoginService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public LoginService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData login(LoginRequest loginRequest) throws UnauthorizedUserError, DataAccessException {
        // get user
        UserData existingUser = userDAO.getUser(loginRequest.username());

        if (existingUser == null) {
            throw new UnauthorizedUserError();
        }

        // verify passwords match
        if (!BCrypt.checkpw(loginRequest.password(), existingUser.password())) {
            throw new UnauthorizedUserError();
        }

        // create new auth
        return authDAO.createAuth(loginRequest.username());
    }
}
