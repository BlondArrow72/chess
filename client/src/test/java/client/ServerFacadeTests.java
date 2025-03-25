package client;

import model.UserData;
import model.AuthData;
import model.CreateGameRequest;

import org.junit.jupiter.api.*;

import server.Server;

import serverFacade.ServerFacade;

public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    @DisplayName("Positive Create Game Test")
    public void createGameSuccess() {
        UserData newUser = new UserData("testUsername", "testPassword", "testEmail");
        AuthData newAuth = facade.register(newUser);

        String authToken = "testAuthToken";
        String gameName = "testGameName";
        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, gameName);

        facade.createGame(createGameRequest);
    }

    @Test
    @DisplayName("Negative Create Game Test")
    public void createGameFailure() {
        CreateGameRequest createGameRequest = new CreateGameRequest("testAuthToken", "testGameName");
        UnauthorizedUserError unauthorizedUserError = Assertions.assertThrows(UnauthorizedUserError.class, () -> {
            service.createGame(createGameRequest);
        });
    }



}
