package service;

import dataaccess.AuthDAO;

public class LogoutService {
    private final AuthDAO authDAO;

    public LogoutService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public void logout(String authToken) throws UnauthorizedUserError {
        try {
            if (authDAO.getAuth(authToken) != null) {
                authDAO.deleteAuth(authToken);
            }
            else {
                throw new UnauthorizedUserError();
            }
        } catch (dataaccess.DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
