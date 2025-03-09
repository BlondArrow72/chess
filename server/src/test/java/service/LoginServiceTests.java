package service;

import dataaccess.UserDAO;
import dataaccess.AuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.MemoryAuthDAO;

import handlers.LoginRequest;
import model.UserData;
import model.AuthData;

import org.junit.jupiter.api.*;

public class LoginServiceTests {
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private LoginService service;

    @BeforeEach
    public void setup() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        service = new LoginService(userDAO, authDAO);
    }

    @AfterEach
    public void clearDAOs() {
        userDAO.clear();
        authDAO.clear();
    }

    @Test
    @DisplayName("Positive Login Test")
    public void loginSuccess() {
        UserData newUser = new UserData("testUsername", "testPassword", "testEmail");
        userDAO.createUser(newUser);

        LoginRequest loginRequest = new LoginRequest("testUsername", "testPassword");
        AuthData newAuth = service.login(loginRequest);

        // assertions
        Assertions.assertEquals(newUser, userDAO.getUser(newUser.username()));
        Assertions.assertEquals(newAuth, authDAO.getAuth(newAuth.authToken()));
    }

    @Test
    @DisplayName("Negative Login Test")
    public void loginFailure() {
        UserData newUser = new UserData("testUsername", "testPassword", "testEmail");
        userDAO.createUser(newUser);

        LoginRequest loginRequest = new LoginRequest("testUsername", "testBadPassword");

        // assertions
        UnauthorizedUserError unauthorizedUserError = Assertions.assertThrows(UnauthorizedUserError.class, () -> {
            service.login(loginRequest);
        });
    }
}
