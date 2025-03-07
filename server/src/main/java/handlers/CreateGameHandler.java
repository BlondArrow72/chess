package handlers;

import dataaccess.GameDAO;
import dataaccess.AuthDAO;

import service.CreateGameService;

import service.UnauthorizedUserError;
import spark.Request;
import spark.Response;

import com.google.gson.Gson;

import java.util.Map;

public class CreateGameHandler {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public CreateGameHandler(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public Object createGame(Request req, Response res) {
        try {
            // deserialize
            String authToken = req.headers("authorization");
            CreateGameRequest createGameRequest = new Gson().fromJson(req.body(), CreateGameRequest.class);
            String gameName = createGameRequest.gameName();

            if (authToken.isEmpty() || gameName.isEmpty()) {
                throw new BadRequestException();
            }

            // call service
            int gameID = new CreateGameService(gameDAO, authDAO).createGame(authToken, gameName);

            // serialize
            res.status(200);
            return new Gson().toJson(Map.of("gameID", gameID));
        }
        catch (BadRequestException e) {
            res.status(400);
            return new Gson().toJson(Map.of("message", e.getMessage()));
        }
        catch (UnauthorizedUserError e) {
            res.status(401);
            return new Gson().toJson(Map.of("message", e.getMessage()));
        }
        catch (Exception e) {
            res.status(500);
            return new Gson().toJson(Map.of("message", e.getMessage()));
        }
    }
}
