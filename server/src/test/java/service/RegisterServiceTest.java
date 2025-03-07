package service;

import dataaccess.UserDAO;
import dataaccess.AuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.MemoryAuthDAO;

import model.UserData;
import model.AuthData;

import org.junit.jupiter.api.*;

public class RegisterServiceTest {
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private RegisterService service;

    @BeforeEach
    public void setup() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        service = new RegisterService(userDAO, authDAO);
    }

    @AfterEach
    public void clearDAOs() {
        userDAO.clear();
        authDAO.clear();
    }

    @Test
    @DisplayName("Positive Register User")
    public void registerSuccess() {
        // create newUser to register
        UserData newUser = new UserData("testUsername", "testPassword", "testEmail");

        // registerUser
        AuthData newAuth = service.register(newUser);

        // assertions
        Assertions.assertEquals(newUser, userDAO.getUser(newUser.username()));
        Assertions.assertEquals(newAuth, authDAO.getAuth(newAuth.authToken()));
    }

    @Test
    @DisplayName("Negative Register User")
    public void registerFailure() {
        // create newUser to register
        UserData newUser = new UserData("testUsername", "testPassword", "testEmail");

        // registerUser
        AuthData successAuth = service.register(newUser);
        Assertions.assertEquals(newUser, userDAO.getUser(newUser.username()));
        Assertions.assertEquals(successAuth, authDAO.getAuth(successAuth.authToken()));

        // assertions
        AlreadyTakenException alreadyTakenException = Assertions.assertThrows(AlreadyTakenException.class, () -> {
            service.register(newUser);
        });
    }
}
