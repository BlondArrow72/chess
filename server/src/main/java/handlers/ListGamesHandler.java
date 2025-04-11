package handlers;

import dataaccess.GameDAO;
import dataaccess.AuthDAO;

import responses.ListGameResponse;
import responses.ListGamesResponse;
import service.ListGamesService;

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
        try {
            // get authToken
            String authToken = req.headers("authorization");

            if (authToken.isEmpty()) {
                throw new BadRequestException();
            }

            // call service
            ListGamesResponse listGamesResponse = new ListGamesService(gameDAO, authDAO).listGames(authToken);

            // return success
            res.status(200);
            return new Gson().toJson(listGamesResponse);
        }
        catch(BadRequestException e) {
            res.status(400);
            return new Gson().toJson(Map.of("message", "Error: bad request"));
        }
        catch(UnauthorizedUserError e) {
            res.status(401);
            return new Gson().toJson(Map.of("message", e.getMessage()));
        }
        catch(Exception e) {
            res.status(500);
            return new Gson().toJson(Map.of("message", e.getMessage()));
        }
    }
}
