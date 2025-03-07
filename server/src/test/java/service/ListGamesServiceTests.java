package service;

import dataaccess.GameDAO;
import dataaccess.AuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryAuthDAO;

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
    public void setup() {
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
        service = new ListGamesService(gameDAO, authDAO);
    }

    @AfterEach
    public void clearDAOs() {
        gameDAO.clear();
        authDAO.clear();
    }

    @Test
    @DisplayName("Positive List Games Test")
    public void listGamesSuccess() {
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
