package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;

public class LogoutService {
    private final AuthDAO authDAO;

    public LogoutService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public void logout(String authToken) throws UnauthorizedUserError, DataAccessException {
        if (authDAO.getAuth(authToken) != null) {
            authDAO.deleteAuth(authToken);
        }
        else {
            throw new UnauthorizedUserError();
        }
    }
}
