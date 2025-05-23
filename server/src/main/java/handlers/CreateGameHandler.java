package handlers;

import dataaccess.GameDAO;
import dataaccess.AuthDAO;

import requests.CreateGameRequest;
import responses.CreateGameResponse;
import service.CreateGameService;

import service.UnauthorizedUserError;
import spark.Request;
import spark.Response;

import com.google.gson.Gson;

import java.util.Map;
import java.util.HashMap;

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
            if (req.body() == null) {
                System.out.println("Null body.");
            }
            HashMap<String, String> gameNameMap = new Gson().fromJson(req.body(), HashMap.class);
            String gameName = gameNameMap.get("gameName");

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
