package service;

import dataaccess.*;

import model.UserData;
import model.GameData;

import chess.ChessGame;

import org.junit.jupiter.api.*;

import javax.xml.crypto.Data;

public class ClearServiceTests {

    @Test
    @DisplayName("Positive Clear Test")
    public void clearSuccess() throws DataAccessException {
        // setup
        UserDAO userDAO = new SQLUserDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        AuthDAO authDAO = new SQLAuthDAO();
        ClearService service = new ClearService(userDAO, gameDAO, authDAO);

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
