package ui;

import chess.ChessGame;
import model.CreateGameRequest;
import model.JoinGameRequest;
import model.ListGamesResponse;

import serverFacade.ServerFacade;

import java.util.Collection;
import java.util.Scanner;
import java.util.HashMap;

public class PostloginUI {
    private final Scanner scanner = new Scanner(System.in);
    private final ServerFacade serverFacade = new ServerFacade();
    private HashMap<Integer, Integer> currentGames = new HashMap<>();

    public JoinGameRequest run(String authToken) {
        printMenu();
        String userResponse = scanner.nextLine();
        JoinGameRequest joinGameRequest = null;

        switch(userResponse) {
            case "Create Game":
                createGame(authToken);

            case "List Games":
                listGames(authToken);

            case "Play Game":
                joinGameRequest = playGame(authToken);

            case "Observe Game":
                joinGameRequest = observeGame(authToken);

            case "Logout":
                logout(authToken);

            case "Help":
                help();

            default:
                System.out.println("Invalid Response. Please try again.");
        }

        return joinGameRequest;
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

    private void createGame(String authToken) {
        System.out.println("What do you want to be the name of your game?");
        String gameName = scanner.nextLine();

        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, gameName);

        serverFacade.createGame(createGameRequest);
    }

    private void listGames(String authToken) {
        Collection<ListGamesResponse> gamesList = serverFacade.listGames(authToken);

        System.out.println("Game Number\tGame Name\tWhite Username\tBlack Username");
        int counter = 1;
        currentGames = new HashMap<>();
        for (ListGamesResponse game : gamesList) {
            currentGames.put(counter, game.gameID());
            System.out.printf("%d/t%s/t%s/t%s%n",
                    counter,
                    game.gameName(),
                    game.whiteUsername(),
                    game.blackUsername()
            );
            counter++;
        }
    }

    private JoinGameRequest playGame(String authToken) {
        listGames(authToken);

        System.out.println("\nWhich game would you like to join?");
        int gameNumber = 0;
        if (scanner.hasNextInt()) {
            gameNumber = scanner.nextInt();
        }
        else {
            System.out.println("Input a valid number.");
            playGame(authToken);
        }

        System.out.println("Would you like to play as 'White' or 'Black'?");
        String teamColor = scanner.nextLine();
        ChessGame.TeamColor playerColor  = ChessGame.TeamColor.WHITE;
        if (teamColor.equals("Black")) {
            playerColor = ChessGame.TeamColor.BLACK;
        }
        else {
            System.out.println("Please join as either 'White' or 'Black'.");
            playGame(authToken);
        }

        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, playerColor, currentGames.get(gameNumber));
        serverFacade.joinGame(joinGameRequest);

        return joinGameRequest;
    }

    private JoinGameRequest observeGame(String authToken) {
        listGames(authToken);

        System.out.println("\nWhich game would you like to observe?");
        int gameNumber = 0;
        if (scanner.hasNextInt()) {
            gameNumber = scanner.nextInt();
        }
        else {
            System.out.println("Input a valid number.");
            playGame(authToken);
        }

        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, ChessGame.TeamColor.WHITE, currentGames.get(gameNumber));
        serverFacade.joinGame(joinGameRequest);

        return joinGameRequest;
    }

    private void logout(String authToken) {
        serverFacade.logout(authToken);
    }

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
