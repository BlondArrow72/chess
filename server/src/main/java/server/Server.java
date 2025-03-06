package server;

import dataaccess.UserDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.GameDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;

import handlers.RegisterHandler;

import spark.Spark;

public class Server {

    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // declare DAO objects
        userDAO = new MemoryUserDAO();
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", (req, res) -> new RegisterHandler(userDAO, authDAO).register(req, res));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
