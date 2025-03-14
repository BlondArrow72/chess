package service;

import dataaccess.*;

import model.UserData;
import model.GameData;

import chess.ChessGame;

import org.junit.jupiter.api.*;

public class ClearServiceTests {

    @Test
    @DisplayName("Positive Clear Test")
    public void clearSuccess() throws DataAccessException {
        // setup
        UserDAO userDAO = new SQLUserDAO();
        GameDAO gameDAO = new SQLGameDAO();
        AuthDAO authDAO = new SQLAuthDAO();
        ClearService service = new ClearService(userDAO, gameDAO, authDAO);

        // add to DAOs
        UserData newUser = new UserData("testUsername", "testPassword", "testEmail");
        userDAO.createUser(newUser);

        String newGameName = "testGameName";
        gameDAO.createGame(newGameName);

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
