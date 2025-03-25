package service;

import dataaccess.*;

import model.JoinGameRequest;
import model.AuthData;

import chess.ChessGame.TeamColor;

import org.junit.jupiter.api.*;

public class JoinGameServiceTests {
    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private JoinGameService service;

    @BeforeEach
    public void setup() throws DataAccessException {
        gameDAO = new SQLGameDAO();
        authDAO = new SQLAuthDAO();
        service = new JoinGameService(gameDAO, authDAO);
    }

    @AfterEach
    public void clearDAOs() throws DataAccessException {
        gameDAO.clear();
        authDAO.clear();
    }

    @Test
    @DisplayName("Positive Join Game Test")
    public void joinGameSuccess() throws DataAccessException {
        AuthData auth = authDAO.createAuth("testUsername");
        int newGameID = gameDAO.createGame("testGameName");

        JoinGameRequest joinGameRequest = new JoinGameRequest(auth.authToken(), TeamColor.WHITE, newGameID);
        service.joinGame(joinGameRequest);

        Assertions.assertEquals("testUsername", gameDAO.getGame(newGameID).whiteUsername());
    }

    @Test
    @DisplayName("Negative Join Game Test")
    public void joinGameFailure() throws DataAccessException {
        AuthData auth = authDAO.createAuth("testUsername");
        String newGameName = "testGameName";
        int gameID = gameDAO.createGame(newGameName);

        JoinGameRequest joinGameRequest = new JoinGameRequest(auth.authToken(), TeamColor.WHITE, gameID);
        service.joinGame(joinGameRequest);

        AlreadyTakenException alreadyTakenException = Assertions.assertThrows(AlreadyTakenException.class, () -> {
            service.joinGame(joinGameRequest);
        });
    }
}
