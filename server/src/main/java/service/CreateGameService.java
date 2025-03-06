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
        if (authDAO.getAuth(authToken) == null) {
            throw new UnauthorizedUserError();
        }

        // create game
        int gameID = gameDAO.getGameID();
        gameDAO.createGame(gameID, null, null, gameName, new ChessGame());

        // return gameID
        return gameID;
    }
}
