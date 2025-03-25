package dataaccess;

import model.ListGamesResponse;
import model.GameData;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

public class GameDAOTests {
    private GameDAO gameDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        gameDAO = new SQLGameDAO();
        gameDAO.clear();
    }

    @Test
    public void createGamePositive() throws DataAccessException {
        String gameName = "createGamePositiveGameName";
        int gameID = gameDAO.createGame(gameName);

        GameData createdGame = gameDAO.getGame(gameID);

        Assertions.assertEquals(gameID, createdGame.gameID());
        Assertions.assertEquals(gameName, createdGame.gameName());
        Assertions.assertNull(createdGame.whiteUsername());
        Assertions.assertNull(createdGame.blackUsername());
    }

    @Test
    public void createGameNegative() throws DataAccessException {
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.createGame(null);
        });
    }

    @Test
    public void getGamePositive() throws DataAccessException {
        String gameName = "getGamePositiveGameName";
        int gameID = gameDAO.createGame(gameName);

        GameData createdGame = gameDAO.getGame(gameID);

        Assertions.assertEquals(gameName, createdGame.gameName());
    }

    @Test
    public void getGameNegative() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.getGame(1234);
        });
    }

    @Test
    public void listGamesPositive() throws DataAccessException {
        gameDAO.clear();
        gameDAO.createGame("gameName1");
        gameDAO.createGame("gameName2");

        Collection<ListGamesResponse> gameList = gameDAO.listGames();

        Assertions.assertEquals(2, gameList.size());
    }

    @Test
    public void listGamesNegative() throws DataAccessException {
        gameDAO.clear();
        Collection<ListGamesResponse> gameList = gameDAO.listGames();
        Assertions.assertTrue(gameList.isEmpty());
    }

    @Test
    public void updateGamePositive() throws DataAccessException {
        String gameName = "updateGamePositiveGameName";
        int gameID = gameDAO.createGame(gameName);
        GameData originalGame = gameDAO.getGame(gameID);

        gameDAO.updateGame(gameID, "whiteUsername", "blackUsername", originalGame.gameName(), originalGame.game());
        GameData updatedGame = gameDAO.getGame(gameID);

        Assertions.assertNotEquals(originalGame, updatedGame);
    }

    @Test
    public void updateGameNegative() {
        String gameName = "updateGameNegativeGameName";
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.updateGame(1234, null, null, gameName, null);
        });
    }

    @Test
    public void clearPositive() throws DataAccessException {
        gameDAO.clear();
        Assertions.assertTrue(gameDAO.isEmpty());
    }

    @Test
    public void isEmptyPositive() throws DataAccessException {
        gameDAO.clear();
        Assertions.assertTrue(gameDAO.isEmpty());
    }

    @Test
    public void isEmptyNegative() throws DataAccessException {
        String gameName = "isEmptyNegativeGameName";
        gameDAO.createGame(gameName);

        Assertions.assertFalse(gameDAO.isEmpty());
    }
}
