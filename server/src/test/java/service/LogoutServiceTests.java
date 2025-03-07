package service;

import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import handlers.LoginRequest;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

public class LogoutServiceTests {
    private AuthDAO authDAO;
    private LogoutService service;

    @BeforeEach
    public void setup() {
        authDAO = new MemoryAuthDAO();
        service = new LogoutService(authDAO);
    }

    @AfterEach
    public void clearDAOs() {
        authDAO.clear();
    }

    @Test
    @DisplayName("Positive Logout Test")
    public void logoutSuccess() {
        AuthData auth = authDAO.createAuth("testUsername");

        Assertions.assertFalse(authDAO.isEmpty());

        service.logout(auth.authToken());

        // assertions
        Assertions.assertTrue(authDAO.isEmpty());

    }

    @Test
    @DisplayName("Negative Logout Test")
    public void logoutFailure() {
        UnauthorizedUserError unauthorizedUserError = Assertions.assertThrows(UnauthorizedUserError.class, () -> {
            service.logout(null);
        });
    }
}
