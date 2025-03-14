package service;

import dataaccess.*;
import handlers.LoginRequest;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

public class LogoutServiceTests {
    private AuthDAO authDAO;
    private LogoutService service;

    @BeforeEach
    public void setup() throws DataAccessException {
        authDAO = new SQLAuthDAO();
        service = new LogoutService(authDAO);
    }

    @AfterEach
    public void clearDAOs() throws DataAccessException {
        authDAO.clear();
    }

    @Test
    @DisplayName("Positive Logout Test")
    public void logoutSuccess() throws DataAccessException {
        AuthData auth = authDAO.createAuth("testUsername");

        Assertions.assertFalse(authDAO.isEmpty());

        service.logout(auth.authToken());

        // assertions
        Assertions.assertTrue(authDAO.isEmpty());

    }

    @Test
    @DisplayName("Negative Logout Test")
    public void logoutFailure() throws DataAccessException {
        UnauthorizedUserError unauthorizedUserError = Assertions.assertThrows(UnauthorizedUserError.class, () -> {
            service.logout(null);
        });
    }
}
