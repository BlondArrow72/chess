package ui;

import chess.ChessBoard;
import chess.ChessGame;

import model.JoinGameRequest;
import serverfacade.ResponseException;

import java.util.Scanner;

public class GameplayUI {
    private final Scanner scanner = new Scanner(System.in);

    public void run(JoinGameRequest joinGameRequest) {
        // start while loop
        while (true) {
            printBoard(joinGameRequest);
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
                run(joinGameRequest);
            }
        }
    }

    private void printBoard(JoinGameRequest joinGameRequest) {
        boolean reverse = (joinGameRequest.playerColor() == ChessGame.TeamColor.BLACK);

        UiChessBoard uiChessBoard = new UiChessBoard();
        ChessBoard defaultBoard = new ChessBoard();
        defaultBoard.resetBoard();

        uiChessBoard.drawBoard(defaultBoard, reverse);
    }

    private void makeMove() {
    }

    private void redrawChessBoard() {

    }

    private void highlightLegalMoves() {

    }

    private void resign() {

    }

    private void leave() {

    }

    private void help() {

    }
}
