package handlers;

import dataaccess.GameDAO;
import dataaccess.AuthDAO;

import service.CreateGameService;

import spark.Request;
import spark.Response;

import com.google.gson.Gson;

public class CreateGameHandler {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public CreateGameHandler(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public Object createGame(Request req, Response res) {
        // deserialize
        String authToken = req.headers("authorization");
        CreateGameRequest createGameRequest = new Gson().fromJson(req.body(), CreateGameRequest.class);
        String gameName = createGameRequest.gameName();

        // call service
        int gameID = new CreateGameService(gameDAO, authDAO).createGame(authToken, gameName);
    }
}
