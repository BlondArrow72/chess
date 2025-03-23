package service;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.AuthDAO;
import handlers.CreateGameRequest;

public class CreateGameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public CreateGameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public int createGame(CreateGameRequest createGameRequest) throws DataAccessException {
        String authToken = createGameRequest.authToken();
        String gameName = createGameRequest.gameName();

        // verify user
        if (authDAO.getAuth(authToken) == null) {
            throw new UnauthorizedUserError();
        }

        // create game
        return gameDAO.createGame(gameName);
    }
}
