package client;

import chess.ChessGame;

import requests.JoinGameRequest;
import requests.ListGamesRequest;
import requests.LoginRequest;
import requests.RegisterRequest;

import responses.ListGameResponse;
import responses.ListGamesResponse;
import responses.LoginResponse;
import responses.RegisterResponse;

import serverfacade.ServerFacade;
import serverfacade.ResponseException;

import server.Server;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;

public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeEach
    public void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
        facade.clear();
    }

    @AfterEach
    void stopServer() {
        facade.clear();
        server.stop();
    }

    @Test
    @DisplayName("Positive Register Test")
    public void registerSuccess() {
        // create newUser to register
        RegisterRequest registerRequest = new RegisterRequest("testPositiveRegisterUsername", "testPositiveRegisterPassword", "testPositiveRegisterEmail");

        // register user
        RegisterResponse registerResponse = facade.register(registerRequest);

        // assertions
        Assertions.assertEquals(registerRequest.username(), registerResponse.username());
    }

    @Test
    @DisplayName("Negative Register Test")
    public void registerFailure() {
        // create new user to register
        RegisterRequest registerRequest = new RegisterRequest("testNegativeRegisterUsername", "testNegativeRegisterPassword", "testNegativeRegisterEmail");

        // registerUser
        RegisterResponse registerResponse = facade.register(registerRequest);
        Assertions.assertEquals(registerRequest.username(), registerResponse.username());

        // assertions
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.register(registerRequest);
        });
    }

    @Test
    @DisplayName("Positive Login Test")
    public void loginSuccess() {
        RegisterRequest registerRequest = new RegisterRequest("testUsernameLoginPositive", "testPassword", "testEmail");
        facade.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("testUsernameLoginPositive", "testPassword");
        LoginResponse loginResponse = facade.login(loginRequest);

        // assertions
        Assertions.assertEquals(loginResponse.username(), loginRequest.username());
    }

    @Test
    @DisplayName("Negative Login Test")
    public void loginFailure() {
        // register new user
        RegisterRequest registerRequest = new RegisterRequest("testUsernameLoginNegative", "testPasswordLoginNegative", "testEmailLoginNegative");
        facade.register(registerRequest);

        // login as the user with a pass password
        LoginRequest loginRequest = new LoginRequest("testUsernameLoginNegative", "testBadPassword");

        // assertions
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.login(loginRequest);
        });
    }

    @Test
    @DisplayName("Positive Logout Test")
    public void logoutSuccess() {
        RegisterRequest  registerRequest  = new RegisterRequest("testUsernameLoginPositive", "testPasswordLoginPositive", "testEmailLoginPositive");
        RegisterResponse registerResponse = facade.register(registerRequest);

        facade.logout(registerResponse.authToken());
    }

    @Test
    @DisplayName("Negative Logout Test")
    public void logoutFailure() {
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.logout(null);
        });
    }

    @Test
    @DisplayName("Positive Create Game Test")
    public void createGameSuccess() {
        RegisterRequest  registerRequest  = new RegisterRequest("testCreateGameSuccessUsername", "testPassword", "testEmail");
        RegisterResponse registerResponse = facade.register(registerRequest);
        String gameName = "testCreateGameSuccessGameName";

        facade.createGame(registerResponse.authToken(), gameName);
    }

    @Test
    @DisplayName("Negative Create Game Test")
    public void createGameFailure() {
        String authToken = "testAuthToken";
        String gameName = "testGameName";
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.createGame(authToken, gameName);
        });
    }

    @Test
    @DisplayName("Positive List Games Test")
    public void listGamesSuccess() {
        // register new user
        RegisterRequest registerRequest = new RegisterRequest("testPositiveListGamesUsername", "testPassword", "testEmail");
        RegisterResponse registerResponse = facade.register(registerRequest);

        // create game to look for
        String gameName = "testGameName";
        facade.createGame(registerResponse.authToken(), gameName);

        // get list of all games
        ListGamesRequest  listGamesRequest  = new ListGamesRequest(registerResponse.authToken());
        ListGamesResponse listGamesResponse = facade.listGames(listGamesRequest);
        Collection<ListGameResponse> gameList = listGamesResponse.games();

        // go through and search for game that matches gameName
        boolean contains = false;
        for (ListGameResponse game : gameList) {
            if (game.whiteUsername() == null
                && game.blackUsername() == null
                && game.gameName().equals(gameName)) {

                // if game exists, change contains to true
                contains = true;
            }
        }

        Assertions.assertTrue(contains);
    }

    @Test
    @DisplayName("Negative List Games Test")
    public void listGamesFailure() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            facade.listGames(null);
        });
    }

    @Test
    @DisplayName("Positive Join Game Test")
    public void joinGameSuccess() {
        // register new user
        RegisterRequest  registerRequest  = new RegisterRequest("testJoinGamePositiveUsername", "testPassword", "testEmail");
        RegisterResponse registerResponse = facade.register(registerRequest);

        // create new game
        String gameName = "testJoinGamePositiveGameName";
        int gameID = facade.createGame(registerResponse.authToken(), gameName);

        // join game just created
        JoinGameRequest joinGameRequest = new JoinGameRequest(registerResponse.authToken(), ChessGame.TeamColor.WHITE, gameID);
        facade.joinGame(joinGameRequest);
    }

    @Test
    @DisplayName("Negative Join Game Test")
    public void joinGameFailure() {
        // register new user
        RegisterRequest  registerRequest  = new RegisterRequest("testJoinGameNegativeUsername", "testPassword", "testEmail");
        RegisterResponse registerResponse = facade.register(registerRequest);

        // create new game
        String newGameName = "testGameName";
        int gameID = facade.createGame(registerResponse.authToken(), newGameName);

        // join new game
        JoinGameRequest joinGameRequest = new JoinGameRequest(registerResponse.authToken(), ChessGame.TeamColor.WHITE, gameID);
        facade.joinGame(joinGameRequest);

        // try to join game again
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.joinGame(joinGameRequest);
        });
    }
}
