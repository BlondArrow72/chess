package service;

import dataaccess.*;

import responses.ListGameResponse;
import responses.ListGamesResponse;
import model.AuthData;

import java.util.Collection;

import org.junit.jupiter.api.*;

public class ListGamesServiceTests {
    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private ListGamesService service;

    @BeforeEach
    public void setup() throws DataAccessException {
        gameDAO = new SQLGameDAO();
        authDAO = new SQLAuthDAO();
        service = new ListGamesService(gameDAO, authDAO);
        gameDAO.clear();
    }

    @AfterEach
    public void clearDAOs() throws DataAccessException {
        gameDAO.clear();
        authDAO.clear();
    }

    @Test
    @DisplayName("Positive List Games Test")
    public void listGamesSuccess() throws DataAccessException {
        AuthData auth = authDAO.createAuth("testUsername");
        gameDAO.createGame("testGameName");

        ListGamesResponse gamesList = service.listGames(auth.authToken());
        Collection<ListGameResponse> gameList = gamesList.games();

        Assertions.assertEquals(1, gameList.size());
    }

    @Test
    @DisplayName("Negative List Games Test")
    public void listGamesFailure() {
        UnauthorizedUserError unauthorizedUserError = Assertions.assertThrows(UnauthorizedUserError.class, () -> {
            service.listGames(null);
        });
    }
}
