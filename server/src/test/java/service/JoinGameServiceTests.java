package service;

import chess.ChessGame;
import dataaccess.GameDAO;
import dataaccess.AuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryAuthDAO;

import model.AuthData;

import chess.ChessGame.TeamColor;

import model.GameData;
import org.junit.jupiter.api.*;

public class JoinGameServiceTests {
    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private JoinGameService service;

    @BeforeEach
    public void setup() {
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
        service = new JoinGameService(gameDAO, authDAO);
    }

    @AfterEach
    public void clearDAOs() {
        gameDAO.clear();
        authDAO.clear();
    }

    @Test
    @DisplayName("Positive Join Game Test")
    public void joinGameSuccess() {
        AuthData auth = authDAO.createAuth("testUsername");
        GameData newGame = new GameData(1234, null, null, "testGameName", new ChessGame());
        gameDAO.createGame(newGame);

        service.joinGame(auth.authToken(), TeamColor.WHITE, 1234);
        GameData game = new GameData(1234, "testUsername", null, "testGameName", new ChessGame());

        Assertions.assertEquals(game, gameDAO.getGame(1234));
    }

    @Test
    @DisplayName("Negative Join Game Test")
    public void joinGameFailure() {
        AuthData auth = authDAO.createAuth("testUsername");
        GameData newGame = new GameData(1234, "taken", null, "testGameName", new ChessGame());
        gameDAO.createGame(newGame);

        AlreadyTakenException alreadyTakenException = Assertions.assertThrows(AlreadyTakenException.class, () -> {
            service.joinGame(auth.authToken(), TeamColor.WHITE, 1234);
        });
    }
}
