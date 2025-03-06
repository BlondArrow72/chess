package service;

import dataaccess.UserDAO;
import dataaccess.GameDAO;
import dataaccess.AuthDAO;

public class ClearService {
    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;

    public ClearService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public void clear() throws RuntimeException {
        if ((userDAO.isEmpty()) && (gameDAO.isEmpty()) && (authDAO.isEmpty())) {
            throw new RuntimeException("Error: databases already clear");
        }
        else {
            userDAO.clear();
            gameDAO.clear();
            authDAO.clear();
        }
    }
}
