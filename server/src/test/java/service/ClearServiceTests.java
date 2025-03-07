package service;

import dataaccess.UserDAO;
import dataaccess.GameDAO;
import dataaccess.AuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryAuthDAO;

import model.UserData;
import model.GameData;

import chess.ChessGame;

import org.junit.jupiter.api.*;

public class ClearServiceTests {
    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private ClearService service;

    @Test
    @DisplayName("Positive Clear Test")
    public void clearSuccess() {
        // setup
        userDAO = new MemoryUserDAO();
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
        service = new ClearService(userDAO, gameDAO, authDAO);

        // add to DAOs
        UserData newUser = new UserData("testUsername", "testPassword", "testEmail");
        userDAO.createUser(newUser);

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
