package ui;

import chess.ChessGame;
import requests.JoinGameRequest;
import responses.ListGameResponse;
import responses.ListGamesResponse;

import serverfacade.ServerFacade;

import java.util.Collection;
import java.util.Scanner;
import java.util.HashMap;

public class PostLoginUI {
    private final Scanner scanner = new Scanner(System.in);
    private final ServerFacade serverFacade = new ServerFacade(8080);

    private PostLoginTicket postloginTicket;
    private HashMap<Integer, Integer> currentGames = new HashMap<>();

    public GameplayTicket run(PostLoginTicket postloginTicket) {
        // make postLoginTicket a class variable
        this.postloginTicket = postloginTicket;

        while (true) {
            // let user know what they can do
            printMenu();

            // get userResponse
            String userResponse = scanner.nextLine();

            // switch on userResponse
            switch (userResponse) {
                case "Create Game"  -> createGame();
                case "List Games"   -> listGames();
                case "Play Game"    -> {return playGame();}
                case "Observe Game" -> {return observeGame();}
                case "Logout"       -> {return logout();}
                case "Help"         -> help();
                default -> System.out.println("Invalid Response. Please try again.");
            }
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

    private void createGame() {
        System.out.println("What do you want to be the name of your game?");
        String gameName = scanner.nextLine();

        serverFacade.createGame(postloginTicket.authToken(), gameName);
    }

    private void listGames() {
        // get games list
        ListGamesResponse listGamesResponse = serverFacade.listGames(postloginTicket.authToken());
        Collection<ListGameResponse> gamesList = listGamesResponse.games();

        // List game data for each game
        System.out.printf("%-12s %-20s %-20s %-20s%n",
                "Game Number", "Game Name", "White Username", "Black Username");
        System.out.println("-".repeat(72));
        int counter = 1;
        currentGames = new HashMap<>();
        for (ListGameResponse game : gamesList) {
            currentGames.put(counter, game.gameID());
            System.out.printf("%-12d %-20s %-20s %-20s %n",
                    counter,
                    truncateString(game.gameName()),
                    truncateString(game.whiteUsername()),
                    truncateString(game.blackUsername())
            );
            counter++;
        }
    }

    private String truncateString(String printString) {
        int maxLength = 20;
        if (printString == null) {
            return "";
        }
        if (printString.length() > maxLength) {
            return printString.substring(0, maxLength - 1);
        }

        return printString;
    }

    private GameplayTicket playGame() {
        int gameNumber = getGameNumber();

        ChessGame.TeamColor playerColor = null;
        while (playerColor == null) {
            System.out.println("Would you like to play as 'White' or 'Black'?");
            String teamColor = scanner.nextLine();

            if (teamColor.equals("White")) {
                playerColor = ChessGame.TeamColor.WHITE;
            } else if (teamColor.equals("Black")) {
                playerColor = ChessGame.TeamColor.BLACK;
            } else {
                System.out.println("Please join as either 'White' or 'Black'.");
            }
        }

        // request to join game
        String authToken = postloginTicket.authToken();
        Integer gameID = currentGames.get(gameNumber);
        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, playerColor, gameID);
        serverFacade.joinGame(joinGameRequest);

        // provide GameplayTicket
        return new GameplayTicket(authToken, playerColor, gameID);
    }

    private GameplayTicket observeGame() {
        int gameNumber = getGameNumber();
        return new GameplayTicket(postloginTicket.authToken(), ChessGame.TeamColor.WHITE, currentGames.get(gameNumber));
    }

    private int getGameNumber() {
        while (true) {
            listGames();
            System.out.println("\nWhich game would you like to join?");

            try {
                String input = scanner.nextLine();
                int gameNumber = Integer.parseInt(input);

                if (currentGames.containsKey(gameNumber)) {
                    return gameNumber;
                }
                else {
                    System.out.println("Please input a valid game number");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please input a valid number.");
            }
        }
    }

    private GameplayTicket logout() {
        serverFacade.logout(postloginTicket.authToken());
        return null;
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
