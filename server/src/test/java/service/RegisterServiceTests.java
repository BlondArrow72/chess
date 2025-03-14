package service;

import dataaccess.*;

import model.UserData;
import model.AuthData;

import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;

public class RegisterServiceTests {
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private RegisterService service;

    @BeforeEach
    public void setup() throws DataAccessException{
        userDAO = new SQLUserDAO();
        authDAO = new SQLAuthDAO();
        service = new RegisterService(userDAO, authDAO);
    }

    @AfterEach
    public void clearDAOs() throws DataAccessException {
        userDAO.clear();
        authDAO.clear();
    }

    @Test
    @DisplayName("Positive Register Test")
    public void registerSuccess() throws DataAccessException {
        // create newUser to register
        UserData newUser = new UserData("testUsername", "testPassword", "testEmail");

        // registerUser
        AuthData newAuth = service.register(newUser);

        // assertions
        Assertions.assertEquals(newUser.username(), userDAO.getUser(newUser.username()).username());
        Assertions.assertTrue(BCrypt.checkpw(newUser.password(), userDAO.getUser(newUser.username()).password()));
        Assertions.assertEquals(newUser.email(), userDAO.getUser(newUser.username()).email());
        Assertions.assertEquals(newAuth, authDAO.getAuth(newAuth.authToken()));
    }

    @Test
    @DisplayName("Negative Register Test")
    public void registerFailure() throws DataAccessException {
        // create newUser to register
        UserData newUser = new UserData("testUsername", "testPassword", "testEmail");

        // registerUser
        AuthData successAuth = service.register(newUser);
        Assertions.assertEquals(newUser.username(), userDAO.getUser(newUser.username()).username());
        Assertions.assertTrue(BCrypt.checkpw(newUser.password(), userDAO.getUser(newUser.username()).password()));
        Assertions.assertEquals(newUser.email(), userDAO.getUser(newUser.username()).email());
        Assertions.assertEquals(successAuth, authDAO.getAuth(successAuth.authToken()));

        // assertions
        AlreadyTakenException alreadyTakenException = Assertions.assertThrows(AlreadyTakenException.class, () -> {
            service.register(newUser);
        });
    }
}
