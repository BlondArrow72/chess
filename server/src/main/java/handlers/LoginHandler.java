package handlers;

import dataaccess.UserDAO;
import dataaccess.AuthDAO;

import model.AuthData;

import model.LoginRequest;
import service.LoginService;

import service.UnauthorizedUserError;
import spark.Request;
import spark.Response;

import com.google.gson.Gson;

import java.util.Map;

public class LoginHandler {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public LoginHandler(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public Object login(Request req, Response res) {
        try {
            // deserialize
            LoginRequest loginRequest = new Gson().fromJson(req.body(), LoginRequest.class);

            // call service
            AuthData auth = new LoginService(userDAO, authDAO).login(loginRequest);

            // serialize
            res.status(200);
            return new Gson().toJson(auth);
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
