package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.ChessMove;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.BLACK_KNIGHT;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ChessBoardUI {
    private PrintStream out;
    private TileColor tileColor;

    private final List<String> headerStrings = new ArrayList<>(List.of(
            EMPTY, " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ", EMPTY
    ));
    private final List<String> rowStrings = new ArrayList<>(List.of(
            " 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "
    ));

    private ChessBoard board;
    private boolean reverse;

    private Collection<ChessPosition> highlightedPositions = new ArrayList<>();

    public enum TileColor {
        LIGHT,
        DARK
    }

    public static void main(String[] args) {
        ChessBoardUI chessBoardUI = new ChessBoardUI();
        ChessBoard chessBoard = new ChessBoard();
        chessBoard.resetBoard();

        Collection<ChessMove> positionsToHighlight = new ArrayList<>();
        ChessPosition chessPosition = new ChessPosition(5, 5);
        ChessPosition newChessPosition = new ChessPosition(5, 6);
        ChessMove chessMove = new ChessMove(chessPosition, newChessPosition);
        positionsToHighlight.add(chessMove);

        chessBoardUI.drawBoard(chessBoard, false, positionsToHighlight);
    }

    public synchronized void drawBoard(ChessBoard board, boolean reverse, Collection<ChessMove> highlightMoves) {
        // do setup
        setUp(board, reverse, highlightMoves);

        // print header
        printHeader();

        // print board
        if (!reverse) {
            for (int i = 7; i > -1; i--) {
                printRow(i);
            }
        }
        else {
            for (int i = 0; i < 8; i++) {
                printRow(i);
            }
        }

        // print header
        printHeader();

        // cleanUp
        cleanUp();
    }

    private void setUp(ChessBoard board, boolean reverse, Collection<ChessMove> highlightMoves) {
        // name printStream out and clear screen
        out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);

        // initialize tileColor variable
        tileColor = TileColor.DARK;

        // ensure text prints out white
        out.print(SET_TEXT_COLOR_WHITE);

        // initialize instance variables
        this.board = board;
        this.reverse = reverse;

        // reverse headers if needed
        if (reverse) {
            Collections.reverse(headerStrings);
            Collections.reverse(rowStrings);
        }

        // store highlighted positions
        storeHighlightedPositions(highlightMoves);
    }

    private void cleanUp() {
        // clear highlighted positions
        highlightedPositions.clear();

        // reset header and row strings
        if (reverse) {
            Collections.reverse(headerStrings);
            Collections.reverse(rowStrings);
        }
    }

    private void storeHighlightedPositions(Collection<ChessMove> highlightMoves) {
        // reset highlighted moves if null
        if (highlightMoves == null) {
            highlightedPositions.clear();
            return;
        }

        // add starting position to highlighedPositions
        ChessMove firstMove = highlightMoves.iterator().next();
        highlightedPositions.add(firstMove.getStartPosition());

        // add all destination moves to highlightedPositions
        for (ChessMove move: highlightMoves) {
            highlightedPositions.add(move.getEndPosition());
        }
    }

    private void printHeader() {
        out.print(SET_BG_COLOR_DARK_GREEN);

        for (String entry : headerStrings) {
            out.print(entry);
        }

        out.print(RESET_BG_COLOR);
        out.println();
    }

    private void printRow(int rowNum) {
        out.print(SET_BG_COLOR_DARK_GREEN);
        if (reverse) {
            out.print(rowStrings.get(7 - rowNum));
        } else {
            out.print(rowStrings.get(rowNum));
        }

        switchTileColor();

        if (tileColor == TileColor.LIGHT) {
            out.print(SET_BG_COLOR_LIGHT_GREY);
        } else {
            out.print(SET_BG_COLOR_DARK_GREY);
        }

        if (reverse) {
            for (int i = 7; i > -1; i--) {
                printRowSquares(rowNum, i);
            }
        }
        else{
            for (int i = 0; i < 8; i++) {
                printRowSquares(rowNum, i);
            }
        }

        out.print(SET_BG_COLOR_DARK_GREEN);
        if (reverse) {
            out.print(rowStrings.get(7 - rowNum));
        } else {
            out.print(rowStrings.get(rowNum));
        }

        out.print(RESET_BG_COLOR);
        out.println();
    }

    private void printRowSquares(int rowNum, int i) {
        // get current position
        ChessPosition currentPosition = new ChessPosition(rowNum + 1, i + 1);

        // highlight current cell
        if (highlightedPositions.contains(currentPosition)) {
            out.print(SET_BG_COLOR_DARK_GREEN);
        }

        // print current piece
        ChessPiece currentPiece = board.getPiece(currentPosition);
        if (currentPiece == null) {
            out.print(EMPTY);
        } else {
            printCurrentPiece(currentPiece);
        }

        switchTileColor();
    }

    private void printCurrentPiece(ChessPiece currentPiece) {
        if (currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            switch (currentPiece.getPieceType()) {
                case BISHOP -> out.print(WHITE_BISHOP);
                case KING -> out.print(WHITE_KING);
                case KNIGHT -> out.print(WHITE_KNIGHT);
                case PAWN -> out.print(WHITE_PAWN);
                case QUEEN -> out.print(WHITE_QUEEN);
                case ROOK -> out.print(WHITE_ROOK);
            }
        } else {
            switch (currentPiece.getPieceType()) {
                case BISHOP -> out.print(BLACK_BISHOP);
                case KING -> out.print(BLACK_KING);
                case KNIGHT -> out.print(BLACK_KNIGHT);
                case PAWN -> out.print(BLACK_PAWN);
                case QUEEN -> out.print(BLACK_QUEEN);
                case ROOK -> out.print(BLACK_ROOK);
            }
        }
    }

    private void switchTileColor() {
        // switch to dark if currently light
        if (tileColor == TileColor.LIGHT) {
            out.print(SET_BG_COLOR_DARK_GREY);
            tileColor = TileColor.DARK;
        } else {
            out.print(SET_BG_COLOR_LIGHT_GREY);
            tileColor = TileColor.LIGHT;
        }
    }
}
