package ui;

import chess.ChessGame;
import chess.ChessPosition;
import chess.ChessMove;
import chess.ChessPiece;
import chess.InvalidMoveException;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Collection;

public class GameplayUI {
    private final Scanner scanner = new Scanner(System.in);
    private final ChessBoardUI chessBoardUI = new ChessBoardUI();

    private GameplayTicket gameplayTicket;
    private ChessGame chessGame;

    public PostLoginTicket run(GameplayTicket gameplayTicket) {
        // make gameplayTicket accessible anywhere in code
        this.gameplayTicket = gameplayTicket;

        // get the current game
        chessGame = new ChessGame();

        // start while loop
        while (true) {
            try {
                // print the board
                printBoard();

                // get user response for menu action
                String userResponse = scanner.nextLine();

                // switch on userResponse
                switch (userResponse) {
                    case "Make Move"             -> makeMove();
                    case "Redraw Chess Board"    -> redrawChessBoard();
                    case "Highlight Legal Moves" -> highlightLegalMoves();
                    case "Resign"                -> resign();
                    case "Leave"                 -> {return leave();}
                    case "Help"                  -> help();
                    default -> System.out.println("Invalid response. Please try again.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void printBoard() {
        // check to see if board should be reversed
        boolean reverse = (gameplayTicket.playerColor() == ChessGame.TeamColor.BLACK);

        // draw the chess board
        chessBoardUI.drawBoard(chessGame.getBoard(), reverse, null);
    }

    private void makeMove() throws InvalidMoveException {
        // ask user which piece they want to move
        System.out.println("Which piece do you want to move? Use this format: A1");
        ChessPosition startingPosition = getValidPiecePositionFromUser();

        // ask user where they want to move the piece to
        System.out.println("Where do you want to move to?");
        String userInput = scanner.nextLine();
        ChessPosition endingPosition = convertInputToPosition(userInput);

        // if piece is a pawn, do special handling elsewhere
        ChessPiece existingPiece = chessGame.getBoard().getPiece(startingPosition);
        ChessMove chessMove;
        if (existingPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
            chessMove = pawnMove(startingPosition, endingPosition, existingPiece.getTeamColor());
        }
        // regular move
        else {
            chessMove = new ChessMove(startingPosition, endingPosition);
        }

        // TODO: verify that chessMove is valid

        // TODO: use WebSocket to make move

        // TODO: use WebSocket to broadcast move
    }

    private ChessMove pawnMove(ChessPosition startingPosition, ChessPosition endingPosition, ChessGame.TeamColor pieceColor) {
        // if piece is white and not in last row
        if (pieceColor == ChessGame.TeamColor.WHITE && endingPosition.getRow() != 8) {
            return new ChessMove(startingPosition, endingPosition);
        }

        // if piece is black and not in last row
        if (pieceColor == ChessGame.TeamColor.BLACK && endingPosition.getRow() != 1) {
            return new ChessMove(startingPosition, endingPosition);
        }

        // ask for promotion type
        System.out.print("Congratulations! Your pawn gets to be promoted!");
        System.out.println("Please select a promotion piece type from the following options:");
        System.out.println("\t-Bishop");
        System.out.println("\t-Knight");
        System.out.println("\t-Queen");
        System.out.println("\t-Rook");
        String userInput = scanner.nextLine();

        // get correct promotion piece type
        ChessPiece.PieceType promotionPiece = null;
        switch(userInput) {
            case "Bishop" -> promotionPiece = ChessPiece.PieceType.BISHOP;
            case "Knight" -> promotionPiece = ChessPiece.PieceType.KNIGHT;
            case "Queen"  -> promotionPiece = ChessPiece.PieceType.QUEEN;
            case "Rook"   -> promotionPiece = ChessPiece.PieceType.ROOK;
            default -> {
                System.out.println("Invalid response. Please try again.");
                pawnMove(startingPosition, endingPosition, pieceColor);
            }
        }

        // return move
        return new ChessMove(startingPosition, endingPosition, promotionPiece);
    }

    private void redrawChessBoard() {
        printBoard();
    }

    private void highlightLegalMoves() {
        // get piece that the user wants to highlight
        System.out.println("What piece moves do you want to see?");
        ChessPosition chessPosition = getValidPiecePositionFromUser();

        // get valid moves for piece in question
        Collection<ChessMove> validMoves = chessGame.validMoves(chessPosition);

        // check to see if board should be reversed
        boolean reverse = (gameplayTicket.playerColor() == ChessGame.TeamColor.BLACK);

        // draw the chess board
        chessBoardUI.drawBoard(chessGame.getBoard(), reverse, validMoves);
    }

    private void resign() {
        // ask user if they're sure they want to resign
        System.out.println("Are you sure you want to resign? (y/n");
        String userInput = scanner.nextLine();

        // check input
        boolean exitLoop = true;
        while (exitLoop) {
            if (userInput.equals("n")) {
                return;
            } else {
                System.out.println("Please input \"y\" or \"n\"");
                exitLoop = false;
            }
        }

        // TODO: use WebSocket to forfeit game

        // TODO: use WebSocket to notify all players that user has resigned
    }

    private PostLoginTicket leave() {
        return new PostLoginTicket(gameplayTicket.authToken());
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

    private ChessPosition getValidPiecePositionFromUser() {
        // loop until get valid position
        ChessPosition chessPosition = null;
        boolean continueLoop = true;
        while (continueLoop) {
            String userInput = scanner.nextLine();
            chessPosition = convertInputToPosition(userInput);

            // check to make sure piece exists on board
            ChessPiece existingPiece = chessGame.getBoard().getPiece(chessPosition);
            if (existingPiece == null) {
                System.out.println("No piece at " + userInput + ". Try again.");
            }
            else {
                continueLoop = false;
            }
        }

        return chessPosition;
    }

    private ChessPosition convertInputToPosition(String userInput) {
        // ensure string has 2 characters and they have the form "A1"
        if (userInput.length() != 2
                || !Character.isLetter(userInput.charAt(0))
                || !Character.isDigit(userInput.charAt(1))) {
            throw new InputMismatchException("Please enter a position resembling \"A1\"");
        }

        // decode characters
        int colNum = userInput.charAt(0);
        int rowNum = userInput.charAt(1);

        // put into ChessPosition object and return
        return new ChessPosition(rowNum, colNum);
    }
}
