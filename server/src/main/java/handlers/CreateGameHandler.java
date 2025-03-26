package handlers;

import dataaccess.GameDAO;
import dataaccess.AuthDAO;

import model.CreateGameRequest;
import model.CreateGameResponse;
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
            String gameName = new Gson().fromJson(req.body(), String.class);

            if (authToken.isEmpty() || gameName.isEmpty()) {
                throw new BadRequestException();
            }

            // call service
            CreateGameRequest createGameRequest = new CreateGameRequest(authToken, gameName);
            int gameID = new CreateGameService(gameDAO, authDAO).createGame(createGameRequest);
            CreateGameResponse createGameResponse = new CreateGameResponse(gameID);

            // serialize
            res.status(200);
            return new Gson().toJson(createGameResponse);
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
