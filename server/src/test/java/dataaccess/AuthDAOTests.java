package dataaccess;

import model.AuthData;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.UnauthorizedUserError;

public class AuthDAOTests {
    private AuthDAO authDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        authDAO = new SQLAuthDAO();
        authDAO.clear();
    }

    @Test
    public void createAuthPositive() throws DataAccessException {
        String testUsername = "creatAuthPositiveUsername";
        AuthData newAuth = authDAO.createAuth(testUsername);
        Assertions.assertEquals(testUsername, newAuth.username());
    }

    @Test
    public void createAuthNegative() throws DataAccessException {
        String testUsername = "createAuthNegativeUsername";
        AuthData firstAuth = authDAO.createAuth(testUsername);

        // I'm not sure how to implement a negative test case for this
        Assertions.assertEquals(testUsername, firstAuth.username());
    }

    @Test
    public void getAuthPositive() throws DataAccessException {
        String testUsername = "getAuthPositiveUsername";
        AuthData testAuth = authDAO.createAuth(testUsername);

        AuthData retrievedAuth = authDAO.getAuth(testAuth.authToken());
        Assertions.assertEquals(testUsername, retrievedAuth.username());
    }

    @Test
    public void getAuthNegative() throws DataAccessException {
        Assertions.assertThrows(UnauthorizedUserError.class, () -> {
           authDAO.getAuth("testAuth");
        });
    }

    @Test
    public void deleteAuthPositive() throws DataAccessException {
        authDAO.clear();
        AuthData createdAuth = authDAO.createAuth("deleteAuthPositiveUsername");
        authDAO.deleteAuth(createdAuth.authToken());
        Assertions.assertTrue(authDAO.isEmpty());
    }

    @Test
    public void deleteAuthNegative() throws DataAccessException {
        Assertions.assertThrows(DataAccessException.class, () -> {
            authDAO.deleteAuth("deleteAuthNegativeAuthToken");
        });
    }

    @Test
    public void clearPositive() throws DataAccessException {
        authDAO.clear();
        Assertions.assertTrue(authDAO.isEmpty());
    }

    @Test
    public void isEmptyPositive() throws DataAccessException {
        authDAO.clear();
        Assertions.assertTrue(authDAO.isEmpty());
    }

    @Test
    public void isEmptyNegative() throws DataAccessException {
        AuthData newAuth = authDAO.createAuth("isEmptyNegativeTest");
        Assertions.assertFalse(authDAO.isEmpty());
    }
}
