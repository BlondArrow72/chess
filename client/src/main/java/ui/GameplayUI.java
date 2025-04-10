package ui;

import chess.ChessBoard;
import chess.ChessGame;

import model.JoinGameRequest;
import serverfacade.ResponseException;

import java.util.Scanner;

public class GameplayUI {
    private final Scanner scanner = new Scanner(System.in);
    private GameplayTicket gameplayTicket;

    public void run(GameplayTicket gameplayTicket) {
        this.gameplayTicket = gameplayTicket;

        // start while loop
        while (true) {
            printBoard();
            String userResponse = scanner.nextLine();

            try {
                switch (userResponse) {
                    case "Make Move" -> {
                        makeMove();
                    }
                    case "Redraw Chess Board" -> {
                        redrawChessBoard();
                    }
                    case "Highlight Legal Moves" -> {
                        highlightLegalMoves();
                    }
                    case "Resign" -> {
                        resign();
                    }
                    case "Leave" -> {
                        leave();
                    }
                    case "Help" -> {
                        help();
                    }
                    default -> {
                        System.out.println("Invalid response. Please try again.");
                    }
                }
            }
            catch (ResponseException e) {
                System.out.println(e.getMessage());
                run(gameplayTicket);
            }
        }
    }

    private void printBoard() {
        boolean reverse = (gameplayTicket.playerColor() == ChessGame.TeamColor.BLACK);

        UiChessBoard uiChessBoard = new UiChessBoard();
        ChessBoard defaultBoard = new ChessBoard();
        defaultBoard.resetBoard();

        uiChessBoard.drawBoard(defaultBoard, reverse);
    }

    private void makeMove() {
    }

    private void redrawChessBoard() {
        printBoard();
    }

    private void highlightLegalMoves() {

    }

    private void resign() {

    }

    private void leave() {

    }

    private void help() {
        System.out.println("Help Menu:");
        System.out.println("Make Move - Allows the player to make a move on their turn.");
        System.out.println("Redraw Chess Board - Shows the user the current state of the chess board.");
        System.out.println("Highlight Legal Moves - Highlights the legal moves of any valid piece.");
        System.out.println("Resign - Allows player to forfeit the game.");
        System.out.println("Leave - Takes the user out of the current game and takes them back to the Post-Login menu.");
        System.out.println("Help - Prints the help menu.");
    }
}
