package service;

import dataaccess.GameDAO;
import dataaccess.AuthDAO;

import model.GameData;

import java.util.Collection;

public class ListGamesService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public ListGamesService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public Collection<GameData> listGames(String authToken) {
        try {
            if (authDAO.getAuth(authToken) == null) {
                throw new UnauthorizedUserError();
            }
        } catch (dataaccess.DataAccessException e) {
            throw new RuntimeException(e);
        }

        return gameDAO.listGames();
    }
}
