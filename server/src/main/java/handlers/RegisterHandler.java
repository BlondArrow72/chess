package handlers;

import dataaccess.UserDAO;
import dataaccess.AuthDAO;

import model.UserData;
import model.AuthData;

import service.RegisterService;
import service.AlreadyTakenException;

import java.util.Map;

import com.google.gson.Gson;

import spark.Request;
import spark.Response;

public class RegisterHandler {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public RegisterHandler(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public Object register(Request req, Response res) {
        try {
            // deserialize req
            UserData user = new Gson().fromJson(req.body(), UserData.class);

            // call service
            AuthData auth = new RegisterService(userDAO, authDAO).register(user);

            // serialize response
            res.status(200);
            res.type("application/json");
            return new Gson().toJson(auth);
        }
        catch(AlreadyTakenException e) {
            res.status(403);
            return new Gson().toJson(Map.of("message", e.getMessage()));
        }
    }
}
