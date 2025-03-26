package ui;

import chess.ChessGame;
import model.CreateGameRequest;
import model.JoinGameRequest;
import model.ListGamesResponse;

import serverFacade.ResponseException;
import serverFacade.ServerFacade;

import java.util.Collection;
import java.util.Scanner;
import java.util.HashMap;

public class PostloginUI {
    private final Scanner scanner = new Scanner(System.in);
    private final ServerFacade serverFacade = new ServerFacade(8080);
    private HashMap<Integer, Integer> currentGames = new HashMap<>();

    public JoinGameRequest run(String authToken) {
        printMenu();
        String userResponse = scanner.nextLine();
        JoinGameRequest joinGameRequest = null;

        try {
            if (userResponse.equals("Create Game")) {
                createGame(authToken);
            }
            else if (userResponse.equals("List Games")) {
                listGames(authToken);
                run(authToken);
            }
            else if (userResponse.equals("Play Game")) {
                joinGameRequest = playGame(authToken);
            }
            else if (userResponse.equals("Observe Game")) {
                joinGameRequest = observeGame(authToken);
            }
            else if (userResponse.equals("Logout")) {
                joinGameRequest = logout(authToken);
            }
            else if (userResponse.equals("Help")) {
                help();
            }
            else {
                System.out.println("Invalid Response. Please try again.");
            }
        }
        catch (ResponseException e) {
            System.out.println(e.getMessage());
            run(authToken);
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

        serverFacade.createGame(authToken, gameName);
    }

    private void listGames(String authToken) {
        Collection<ListGamesResponse> gamesList = serverFacade.listGames(authToken);

        System.out.println("Game Number     Game Name       White Username      Black Username");
        int counter = 1;
        currentGames = new HashMap<>();
        for (ListGamesResponse game : gamesList) {
            currentGames.put(counter, game.gameID());
            System.out.printf("%d\t\t%s\t\t%s\t\t%s%n",
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
            scanner.nextLine();
        }
        else {
            System.out.println("Input a valid number.");
            playGame(authToken);
        }

        System.out.println("Would you like to play as 'White' or 'Black'?");
        String teamColor = scanner.nextLine();
        ChessGame.TeamColor playerColor = null;
        if (teamColor.equals("White")) {
            playerColor  = ChessGame.TeamColor.WHITE;
        }
        else if (teamColor.equals("Black")) {
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
            scanner.nextLine();
        }
        else {
            System.out.println("Input a valid number.");
            playGame(authToken);
        }

        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, ChessGame.TeamColor.WHITE, currentGames.get(gameNumber));

        return joinGameRequest;
    }

    private JoinGameRequest logout(String authToken) {
        serverFacade.logout(authToken);
        return new JoinGameRequest(null, null, null);
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
