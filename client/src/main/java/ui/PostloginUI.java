package ui;

import model.CreateGameRequest;
import model.ListGamesResponse;

import serverFacade.ServerFacade;

import java.util.Collection;
import java.util.Scanner;

public class PostloginUI {
    private final Scanner scanner = new Scanner(System.in);
    private final ServerFacade serverFacade = new ServerFacade();

    public void run(String authToken) {
        printMenu();
        String userResponse = scanner.nextLine();

        switch(userResponse) {
            case "Create Game":
                int gameID = createGame(authToken);

            case "List Games":
                help(); // listGames(authToken);

            case "Play Game":
                help(); // playGame();

            case "Observe Game":
                help(); // observeGame();

            case "Logout":
                help(); // logout();

            case "Help":
                help();

            default:
                System.out.println("Invalid Response.");
        }
    }

    private void printMenu() {
        System.out.println("Choose an option:");
        System.out.println();

        System.out.println("Create Game");
        System.out.println("List Games");
        System.out.println("Play Game");
        System.out.println("Observe Game");
        System.out.println("Logout");
        System.out.println("Help");
        System.out.println();

        System.out.println("Hit ENTER after typing your selection.");
    }

    private int createGame(String authToken) {
        System.out.println("What do you want to be the name of your game?");
        String gameName = scanner.nextLine();

        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, gameName);

        return serverFacade.createGame(createGameRequest);
    }

    /*
    private void listGames(String authToken) {
        Collection<ListGamesResponse> gamesList = serverFacade.listGames(authToken);
        for (ListGamesResponse game : gamesList) {
            game.toString();
        }
    }

     */

    /*
    private void playGame() {
        return serverFacade.playGame();
    }

    private void observeGame() {
        return serverFacade.observeGame();
    }

    private void logout() {
        return serverFacade.logout();
    }
    */

    private void help() {
        System.out.println("Help Menu:");
        System.out.println("Create Game - Allows the user to input a name for the new game.");
        System.out.println("List Games - Lists all the games that currently exist on the server.");
        System.out.println("Play Game - Allows the user to specify which game they want to join and what color they want to play.");
        System.out.println("Observe Game - Allows the user to specify which game they want to observe.");
        System.out.println("Logout - Logs out the user.");
        System.out.println("Help - Displays help text.");
        System.out.println();
    }
}
