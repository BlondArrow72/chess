package service;

import dataaccess.*;

import handlers.LoginRequest;
import model.UserData;
import model.AuthData;

import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;

public class LoginServiceTests {
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private LoginService service;

    @BeforeEach
    public void setup() throws DataAccessException {
        userDAO = new SQLUserDAO();
        authDAO = new SQLAuthDAO();
        service = new LoginService(userDAO, authDAO);
    }

    @AfterEach
    public void clearDAOs() throws DataAccessException {
        userDAO.clear();
        authDAO.clear();
    }

    @Test
    @DisplayName("Positive Login Test")
    public void loginSuccess() throws DataAccessException {
        UserData newUser = new UserData("testUsernamePositive", "testPassword", "testEmail");
        userDAO.createUser(newUser);

        LoginRequest loginRequest = new LoginRequest("testUsernamePositive", "testPassword");
        AuthData newAuth = service.login(loginRequest);

        UserData loggedInUser = userDAO.getUser(loginRequest.username());

        // assertions
        Assertions.assertTrue(BCrypt.checkpw(newUser.password(), loggedInUser.password()));
        Assertions.assertEquals(newAuth, authDAO.getAuth(newAuth.authToken()));
    }

    @Test
    @DisplayName("Negative Login Test")
    public void loginFailure() throws DataAccessException {
        UserData newUser = new UserData("testUsernameNegative", "testPassword", "testEmail");
        userDAO.createUser(newUser);

        LoginRequest loginRequest = new LoginRequest("testUsernameNegative", "testBadPassword");

        // assertions
        UnauthorizedUserError unauthorizedUserError = Assertions.assertThrows(UnauthorizedUserError.class, () -> {
            service.login(loginRequest);
        });
    }
}
