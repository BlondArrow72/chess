package client;

import chess.ChessGame;

import model.UserData;
import model.AuthData;
import requests.JoinGameRequest;
import responses.ListGameResponse;
import responses.ListGamesResponse;
import requests.LoginRequest;

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
        UserData newUser = new UserData("testPositiveRegisterUsername", "testPositiveRegisterPassword", "testPositiveRegisterEmail");

        // registerUser
        AuthData newAuth = facade.register(newUser);

        // assertions
        Assertions.assertEquals(newUser.username(), newAuth.username());
    }

    @Test
    @DisplayName("Negative Register Test")
    public void registerFailure() {
        // create newUser to register
        UserData newUser = new UserData("testNegativeRegisterUsername", "testNegativeRegisterPassword", "testNegativeRegisterEmail");

        // registerUser
        AuthData successAuth = facade.register(newUser);
        Assertions.assertEquals(newUser.username(), successAuth.username());

        // assertions
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.register(newUser);
        });
    }

    @Test
    @DisplayName("Positive Login Test")
    public void loginSuccess() {
        UserData newUser = new UserData("testUsernameLoginPositive", "testPassword", "testEmail");
        facade.register(newUser);

        LoginRequest loginRequest = new LoginRequest("testUsernameLoginPositive", "testPassword");
        AuthData newAuth = facade.login(loginRequest);

        // assertions
        Assertions.assertEquals(newAuth.username(), newUser.username());
    }

    @Test
    @DisplayName("Negative Login Test")
    public void loginFailure() {
        UserData newUser = new UserData("testUsernameLoginNegative", "testPasswordLoginNegative", "testEmailLoginNegative");
        facade.register(newUser);

        LoginRequest loginRequest = new LoginRequest("testUsernameLoginNegative", "testBadPassword");

        // assertions
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.login(loginRequest);
        });
    }

    @Test
    @DisplayName("Positive Logout Test")
    public void logoutSuccess() {
        UserData newUser = new UserData("testUsernameLoginPositive", "testPasswordLoginPositive", "testEmailLoginPositive");
        AuthData auth = facade.register(newUser);

        facade.logout(auth.authToken());
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
        UserData newUser = new UserData("testCreateGameSuccessUsername", "testPassword", "testEmail");
        AuthData newAuth = facade.register(newUser);
        String gameName = "testCreateGameSuccessGameName";

        facade.createGame(newAuth.authToken(), gameName);
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
        UserData newUser = new UserData("testPositiveListGamesUsername", "testPassword", "testEmail");
        AuthData auth = facade.register(newUser);

        String gameName = "testGameName";
        int gameID = facade.createGame(auth.authToken(), gameName);

        ListGamesResponse listGamesResponse = facade.listGames(auth.authToken());
        Collection<ListGameResponse> gameList = listGamesResponse.games();

        for (ListGameResponse game : gameList) {
            if (game.whiteUsername() == null
                && game.blackUsername() == null
                && game.gameName().equals(gameName)) {

                Assertions.assertTrue(true);
            }
        }
    }

    @Test
    @DisplayName("Negative List Games Test")
    public void listGamesFailure() {
        Assertions.assertThrows(ResponseException.class, () -> {
            facade.listGames(null);
        });
    }

    @Test
    @DisplayName("Positive Join Game Test")
    public void joinGameSuccess() {
        UserData newUser = new UserData("testJoinGamePositiveUsername", "testPassword", "testEmail");
        AuthData auth = facade.register(newUser);

        String gameName = "testJoinGamePositiveGameName";
        int gameID = facade.createGame(auth.authToken(), gameName);

        JoinGameRequest joinGameRequest = new JoinGameRequest(auth.authToken(), ChessGame.TeamColor.WHITE, gameID);
        facade.joinGame(joinGameRequest);

        Assertions.assertEquals("testJoinGamePositiveUsername", auth.username());
    }

    @Test
    @DisplayName("Negative Join Game Test")
    public void joinGameFailure() {
        UserData newUser = new UserData("testJoinGameNegativeUsername", "testPassword", "testEmail");
        AuthData auth = facade.register(newUser);

        String newGameName = "testGameName";
        int gameID = facade.createGame(auth.authToken(), newGameName);

        JoinGameRequest joinGameRequest = new JoinGameRequest(auth.authToken(), ChessGame.TeamColor.WHITE, gameID);
        facade.joinGame(joinGameRequest);

        Assertions.assertThrows(ResponseException.class, () -> {
            facade.joinGame(joinGameRequest);
        });
    }
}
