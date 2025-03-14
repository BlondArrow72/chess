package service;

import dataaccess.*;

import handlers.ListGamesResponse;
import model.GameData;
import model.AuthData;

import chess.ChessGame;

import java.util.ArrayList;
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

        Collection<ListGamesResponse> gameList = service.listGames(auth.authToken());

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
