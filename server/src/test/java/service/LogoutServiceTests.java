package service;

import dataaccess.*;
import model.AuthData;
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
        // start with empty authDAO
        authDAO.clear();

        // add one auth
        AuthData auth = authDAO.createAuth("testUsername");

        // ensure authDAO not empty
        Assertions.assertFalse(authDAO.isEmpty());

        // clear the auth
        service.logout(auth.authToken());

        // assert authDAO as empty
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
