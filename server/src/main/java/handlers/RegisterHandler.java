package handlers;

import dataaccess.UserDAO;
import dataaccess.AuthDAO;

import model.UserData;
import model.AuthData;

import service.UserService;

import com.google.gson.Gson;

import spark.Request;
import spark.Response;

public class RegisterHandler {
    private UserDAO userDAO;
    private AuthDAO authDAO;

    public RegisterHandler(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public Object register(Request req, Response res) {
        // deserialize req
        UserData user = new Gson().fromJson(req.body(), UserData.class);

        // call service
        AuthData auth = new UserService(userDAO, authDAO).register(user);

        // serialize response
        res.status(200);
        res.type("application/json");
        return new Gson().toJson(auth);
    }
}
