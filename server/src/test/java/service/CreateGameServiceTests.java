package service;

import dataaccess.*;

import model.AuthData;

import org.junit.jupiter.api.*;

public class CreateGameServiceTests {
    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private CreateGameService service;

    @BeforeEach
    public void setup() throws DataAccessException {
        gameDAO = new SQLGameDAO();
        authDAO = new SQLAuthDAO();
        service = new CreateGameService(gameDAO, authDAO);
    }

    @AfterEach
    public void clearDAOs() throws DataAccessException {
        gameDAO.clear();
        authDAO.clear();
    }

    @Test
    @DisplayName("Positive Create Game Test")
    public void createGameSuccess() throws DataAccessException {
        AuthData auth = authDAO.createAuth("testUsername");
        int gameID = service.createGame(auth.authToken(), "testGame");

        Assertions.assertEquals(gameID, gameDAO.getGame(gameID).gameID());
    }

    @Test
    @DisplayName("Negative Create Game Test")
    public void createGameFailure() {
        UnauthorizedUserError unauthorizedUserError = Assertions.assertThrows(UnauthorizedUserError.class, () -> {
            service.createGame(null, "testGameName");
        });
    }
}
