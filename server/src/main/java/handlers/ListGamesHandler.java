package handlers;

import dataaccess.GameDAO;
import dataaccess.AuthDAO;

import service.ListGamesService;

import model.GameData;
import service.UnauthorizedUserError;
import spark.Request;
import spark.Response;

import com.google.gson.Gson;

import java.util.Collection;
import java.util.Map;

public class ListGamesHandler {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public ListGamesHandler(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public Object listGames(Request req, Response res) throws UnauthorizedUserError {
        // get authToken
        String authToken = req.headers("authorization");

        try {
            // call service
            Collection<GameData> gameList = new ListGamesService(gameDAO, authDAO).listGames(authToken);

            // return success
            res.status(200);
            return new Gson().toJson(gameList);
        }
        catch(UnauthorizedUserError e) {
            res.status(401);
            return new Gson().toJson(Map.of("message", e.getMessage()));
        }
    }
}
