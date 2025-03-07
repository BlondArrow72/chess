package service;

import dataaccess.GameDAO;
import dataaccess.AuthDAO;

import model.GameData;
import model.AuthData;

import chess.ChessGame;

public class JoinGameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public JoinGameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public void joinGame(String authToken, ChessGame.TeamColor playerColor, int gameID) {
        // verify authToken
        if (authDAO.getAuth(authToken) == null) {
            throw new UnauthorizedUserError();
        }

        // get authorization
        AuthData auth = authDAO.getAuth(authToken);

        // get game
        GameData game = gameDAO.getGame(gameID);

        // determine playerColor
        if (playerColor == ChessGame.TeamColor.WHITE) {
            // verify empty spot
            if (game.whiteUsername() != null) {
                throw new AlreadyTakenException();
            }

            // update game
            gameDAO.updateGame(game.gameID(), auth.username(), game.blackUsername(), game.gameName(), game.game());
        }
        else {
            // verify empty spot
            if (game.blackUsername() != null) {
                throw new AlreadyTakenException();
            }

            // update game
            gameDAO.updateGame(game.gameID(), game.whiteUsername(), auth.username(), game.gameName(), game.game());
        }
    }
}
