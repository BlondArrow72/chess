package handlers;

import dataaccess.AuthDAO;

import service.LogoutService;

import service.UnauthorizedUserError;
import spark.Request;
import spark.Response;

import com.google.gson.Gson;

import java.util.Map;

public class LogoutHandler {
    private final AuthDAO authDAO;

    public LogoutHandler(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public Object logout(Request req, Response res) {
        try{
            // deserialize
            String authToken = req.headers("authorization");

            // call service
            new LogoutService(authDAO).logout(authToken);

            res.status(200);
            return new Gson().toJson(null);
        }
        catch (UnauthorizedUserError e) {
            res.status(401);
            return new Gson().toJson(Map.of("message", e.getMessage()));
        }

    }
}
