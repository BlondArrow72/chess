package handlers;

import dataaccess.UserDAO;
import dataaccess.GameDAO;
import dataaccess.AuthDAO;

import service.ClearService;

import java.util.Map;

import com.google.gson.Gson;

import spark.Request;
import spark.Response;

public class ClearHandler {
    private final UserDAO userDAO;
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public ClearHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public Object clear(Request req, Response res) {
        try {
            new ClearService(userDAO, gameDAO, authDAO).clear();
            res.status(200);
            return new Gson().toJson(null);
        }
        catch(Exception e) {
            res.status(500);
            return new Gson().toJson(Map.of("message", e.getMessage()));
        }
    }
}
