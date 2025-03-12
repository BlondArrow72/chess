package service;

import dataaccess.*;

import model.UserData;
import model.GameData;

import chess.ChessGame;

import org.junit.jupiter.api.*;

import javax.xml.crypto.Data;

public class ClearServiceTests {
    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private ClearService service;

    @Test
    @DisplayName("Positive Clear Test")
    public void clearSuccess() throws DataAccessException {
        // setup
        userDAO = new MemoryUserDAO();
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
        service = new ClearService(userDAO, gameDAO, authDAO);

        // add to DAOs
        UserData newUser = new UserData("testUsername", "testPassword", "testEmail");
        try {
            userDAO.createUser(newUser);
        } catch (dataaccess.DataAccessException e) {
            throw new RuntimeException(e);
        }

        GameData newGame = new GameData(1234, null, null, "testGameName", new ChessGame());
        gameDAO.createGame(newGame);

        authDAO.createAuth("testUsername");

        // assertions
        Assertions.assertNotNull(userDAO);
        Assertions.assertNotNull(gameDAO);
        Assertions.assertNotNull(authDAO);

        // call service
        service.clear();

        // assertions
        Assertions.assertTrue(userDAO.isEmpty());
        Assertions.assertTrue(gameDAO.isEmpty());
        Assertions.assertTrue(authDAO.isEmpty());
    }
}
