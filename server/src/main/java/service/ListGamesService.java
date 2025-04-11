package service;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.AuthDAO;

import responses.ListGameResponse;
import responses.ListGamesResponse;

import java.util.Collection;

public class ListGamesService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public ListGamesService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public ListGamesResponse listGames(String authToken) throws DataAccessException {
        // verify authorized user
        if (authDAO.getAuth(authToken) == null) {
            throw new UnauthorizedUserError();
        }

        // get collection of games
        Collection<ListGameResponse> gamesList  = gameDAO.listGames();

        // refactor into a ListGamesResponse object
        return new ListGamesResponse(gamesList);
    }
}
