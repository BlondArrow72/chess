package service;

import chess.ChessGame;
import dataaccess.GameDAO;
import dataaccess.AuthDAO;
import model.GameData;

public class CreateGameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public CreateGameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public int createGame(String authToken, String gameName) {
        // verify user
        try {
            if (authDAO.getAuth(authToken) == null) {
                throw new UnauthorizedUserError();
            }
        } catch (dataaccess.DataAccessException e) {
            throw new RuntimeException(e);
        }

        // create game
        int gameID = gameDAO.getGameID();
        GameData newGame = new GameData(gameID, null, null, gameName, new ChessGame());
        gameDAO.createGame(newGame);

        // return gameID
        return gameID;
    }
}
