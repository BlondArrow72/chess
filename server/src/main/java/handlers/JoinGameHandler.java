package handlers;

import dataaccess.GameDAO;
import dataaccess.AuthDAO;

import spark.Request;
import spark.Response;

import com.google.gson.Gson;

import java.util.Map;

public class JoinGameHandler {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public JoinGameHandler(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public Object joinGame(Request req, Response res) {
        try {
            // deserialize
            String authToken = req.headers("authorization");
            JoinGameRequest joinGameRequest = new Gson().fromJson(req.body(), JoinGameRequest.class);

            if (authToken.isEmpty() || joinGameRequest.playerColor() == null || joinGameRequest.gameID() == null) {
                throw new BadRequestException();
            }

            // send to service

            // return response
        }
        catch (BadRequestException e) {
            res.status(400);
            return new Gson().toJson(Map.of("message", e.getMessage()));
        }
    }
}
