package service;

import dataaccess.*;

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
        gameDAO = new MemoryGameDAO();
        authDAO = new SQLAuthDAO();
        service = new ListGamesService(gameDAO, authDAO);
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
        GameData newGame = new GameData(1234, null, null, "testGameName", new ChessGame());
        gameDAO.createGame(newGame);

        Collection<GameData> gameList = service.listGames(auth.authToken());

        Collection<GameData> expected = new ArrayList<>();
        expected.add(newGame);

        Assertions.assertEquals(gameList.size(), expected.size());
    }

    @Test
    @DisplayName("Negative List Games Test")
    public void listGamesFailure() {
        UnauthorizedUserError unauthorizedUserError = Assertions.assertThrows(UnauthorizedUserError.class, () -> {
            service.listGames(null);
        });
    }
}
