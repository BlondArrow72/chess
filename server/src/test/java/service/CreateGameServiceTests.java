package service;

import dataaccess.GameDAO;
import dataaccess.AuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryAuthDAO;

import model.AuthData;

import org.junit.jupiter.api.*;

public class CreateGameServiceTests {
    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private CreateGameService service;

    @BeforeEach
    public void setup() {
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
        service = new CreateGameService(gameDAO, authDAO);
    }

    @AfterEach
    public void clearDAOs() {
        gameDAO.clear();
        authDAO.clear();
    }

    @Test
    @DisplayName("Positive Create Game Test")
    public void createGameSuccess() {
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
