package service;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;

import model.AuthData;
import model.UserData;

public class RegisterService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public RegisterService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData register(UserData newUser) throws AlreadyTakenException, DataAccessException {
        // check if username is already taken
        try {
            if (userDAO.getUser(newUser.username()) != null) {
                throw new AlreadyTakenException();
            }
        } catch (dataaccess.DataAccessException e) {
            throw new RuntimeException(e);
        }

        // createUser
        try {
            userDAO.createUser(newUser);
        } catch (dataaccess.DataAccessException e) {
            throw new RuntimeException(e);
        }

        // createAuth
        return authDAO.createAuth(newUser.username());
    }
}
