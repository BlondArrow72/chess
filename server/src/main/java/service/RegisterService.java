package service;

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

    public AuthData register(UserData newUser) throws AlreadyTakenException {
        // check if username is already taken
        if (userDAO.getUser(newUser.username()) != null) {
            throw new AlreadyTakenException();
        }

        // createUser
        userDAO.createUser(newUser);

        // createAuth
        return authDAO.createAuth(newUser.username());
    }
}
