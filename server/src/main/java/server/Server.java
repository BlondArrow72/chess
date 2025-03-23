package server;

import dataaccess.*;

import handlers.RegisterHandler;
import handlers.ClearHandler;
import handlers.LoginHandler;
import handlers.LogoutHandler;
import handlers.ListGamesHandler;
import handlers.CreateGameHandler;
import handlers.JoinGameHandler;

import spark.Spark;

public class Server {

    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        try {
            // declare DAO objects
            userDAO = new SQLUserDAO();
            gameDAO = new SQLGameDAO();
            authDAO = new SQLAuthDAO();
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        // Register
        Spark.post("/user", (req, res) -> new RegisterHandler(userDAO, authDAO).register(req, res));

        // Clear
        Spark.delete("/db", (req, res) -> new ClearHandler(userDAO, gameDAO, authDAO).clear(req, res));

        // Login
        Spark.post("/session", (req, res) -> new LoginHandler(userDAO, authDAO).login(req, res));

        // Logout
        Spark.delete("/session", (req, res) -> new LogoutHandler(authDAO).logout(req, res));

        // List Games
        Spark.get("/game", (req, res) -> new ListGamesHandler(gameDAO, authDAO).listGames(req, res));

        // Create Game
        Spark.post("/game", (req, res) -> new CreateGameHandler(gameDAO, authDAO).createGame(req, res));

        // Join Game
        Spark.put("/game", (req, res) -> new JoinGameHandler(gameDAO, authDAO).joinGame(req, res));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
