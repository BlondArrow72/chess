package dataaccess;

import model.AuthData;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AuthDAOTests {
    private AuthDAO authDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        authDAO = new SQLAuthDAO();
        authDAO.clear();
    }

    /*
    @Test
    public void createAuthPositive throws DataAccessException;

    @Test
    public void createAuthNegative throws DataAccessException;

    @Test
    public void getAuthPositive throws DataAccessException;

    @Test
    public void getAuthNegative throws DataAccessException;

    @Test
    public void deleteAuthPositive throws DataAccessException;

    @Test
    public void deleteAuthNegative throws DataAccessException;

    @Test
    public void clearPositive throws DataAccessException {
        authDAO.clear();
        Assertions.assertTrue(authDAO.isEmpty());
    }

    @Test
    public void isEmptyPositive throws DataAccessException {
        authDAO.clear();
        Assertions.assertTrue(authDAO.isEmpty());
    }

    @Test
    public void isEmptyNegative throws DataAccessException {

    }
    */
}
