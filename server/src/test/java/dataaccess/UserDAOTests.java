package dataaccess;

import model.UserData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.mindrot.jbcrypt.BCrypt;
import service.AlreadyTakenException;

public class UserDAOTests {
    private UserDAO userDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        userDAO = new SQLUserDAO();
        userDAO.clear();
    }

    @Test
    public void createUserPositive() throws DataAccessException {
        UserData newUser = new UserData("testCreateUserPositive", "testPassword", "testEmail");
        userDAO.createUser(newUser);

        UserData createdUser = userDAO.getUser(newUser.username());
        Assertions.assertTrue(BCrypt.checkpw(newUser.password(), createdUser.password()));
    }

    @Test
    public void createUserNegative() throws DataAccessException {
        UserData newUser = new UserData("testCreateUserNegative", "testPassword", "testEmail");
        userDAO.createUser(newUser);

        DataAccessException dataAccessException = Assertions.assertThrows(DataAccessException.class, () -> {
            userDAO.createUser(newUser);
        });
    }

    @Test
    public void getUserPositive() throws DataAccessException {
        UserData newUser = new UserData("testGetUserPositive", "testPassword", "testEmail");
        userDAO.createUser(newUser);

        UserData retrievedUser = userDAO.getUser(newUser.username());

        Assertions.assertEquals(newUser.username(), retrievedUser.username());
        Assertions.assertTrue(BCrypt.checkpw(newUser.password(), retrievedUser.password()));
        Assertions.assertEquals(newUser.email(), retrievedUser.email());
    }

    @Test
    public void getUserNegative() throws DataAccessException {
        DataAccessException dataAccessException = Assertions.assertThrows(DataAccessException.class, () -> {
            userDAO.getUser("testGetUserNegative");
        });
    }

    @Test
    public void clearPositive() throws DataAccessException {
        userDAO.clear();

        Assertions.assertTrue(userDAO.isEmpty());
    }

    @Test
    public void isEmptyPositive() throws DataAccessException {
        userDAO.clear();

        Assertions.assertTrue(userDAO.isEmpty());
    }

    @Test
    public void isEmptyNegative() throws DataAccessException {
        UserData newUser = new UserData("testIsEmptyNegative", "testPassword", "testEmail");
        userDAO.createUser(newUser);

        Assertions.assertFalse(userDAO.isEmpty());
    }
}
