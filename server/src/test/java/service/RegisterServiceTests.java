package service;

import dataaccess.*;

import model.UserData;
import model.AuthData;

import org.junit.jupiter.api.*;

public class RegisterServiceTests {
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private RegisterService service;

    @BeforeEach
    public void setup() throws DataAccessException{
        userDAO = new SQLUserDAO();
        authDAO = new MemoryAuthDAO();
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
        try {
            Assertions.assertEquals(newUser, userDAO.getUser(newUser.username()));
        } catch (dataaccess.DataAccessException e) {
            throw new RuntimeException(e);
        }
        try {
            Assertions.assertEquals(newAuth, authDAO.getAuth(newAuth.authToken()));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Negative Register Test")
    public void registerFailure() throws DataAccessException {
        // create newUser to register
        UserData newUser = new UserData("testUsername", "testPassword", "testEmail");

        // registerUser
        AuthData successAuth = service.register(newUser);
        try {
            Assertions.assertEquals(newUser, userDAO.getUser(newUser.username()));
        } catch (dataaccess.DataAccessException e) {
            throw new RuntimeException(e);
        }
        try {
            Assertions.assertEquals(successAuth, authDAO.getAuth(successAuth.authToken()));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        // assertions
        AlreadyTakenException alreadyTakenException = Assertions.assertThrows(AlreadyTakenException.class, () -> {
            service.register(newUser);
        });
    }
}
