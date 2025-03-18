package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.BLACK_KNIGHT;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UiChessBoard {
    private PrintStream out;
    private TileColor tileColor;

    private List<String> headerStrings = new ArrayList<>(List.of(
            EMPTY, " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ", EMPTY
    ));
    private List<String> rowStrings = new ArrayList<>(List.of(
            " 8 ", " 7 ", " 6 ", " 5 ", " 4 ", " 3 ", " 2 ", " 1 "
    ));

    private ChessBoard board;
    private boolean reverse;

    public enum TileColor {
        LIGHT,
        DARK
    }

    public static void main(String[] args) {
        System.out.println("Chess Board" + WHITE_KING + "\n");

        UiChessBoard uiChessBoard = new UiChessBoard();
        ChessBoard chessBoard = new ChessBoard();
        chessBoard.resetBoard();

        uiChessBoard.drawBoard(chessBoard, false);
    }

    public void drawBoard(ChessBoard board, boolean reverse) {
        out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);

        tileColor = TileColor.DARK;

        out.print(SET_TEXT_COLOR_WHITE);

        this.board = board;
        this.reverse = reverse;

        if (reverse) {
            Collections.reverse(headerStrings);
        }

        // print header
        printHeader();

        // print board
        if (reverse) {
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
        out.print(rowStrings.get(rowNum));

        switchTileColor();

        if (tileColor == TileColor.LIGHT) {
            out.print(SET_BG_COLOR_LIGHT_GREY);
        } else {
            out.print(SET_BG_COLOR_DARK_GREY);
        }

        if (reverse) {
            for (int i = 7; i > -1; i--) {
                ChessPosition currentPosition = new ChessPosition(rowNum + 1, i + 1);
                ChessPiece currentPiece = board.getPiece(currentPosition);

                if (currentPiece == null) {
                    out.print(EMPTY);
                } else {
                    printCurrentPiece(currentPiece);
                }

                switchTileColor();
            }
        }
        else{
            for (int i = 0; i < 8; i++) {
                ChessPosition currentPosition = new ChessPosition(rowNum + 1, i + 1);
                ChessPiece currentPiece = board.getPiece(currentPosition);

                if (currentPiece == null) {
                    out.print(EMPTY);
                } else {
                    printCurrentPiece(currentPiece);
                }

                switchTileColor();
            }
        }

        out.print(SET_BG_COLOR_DARK_GREEN);
        out.print(rowStrings.get(rowNum));

        out.print(RESET_BG_COLOR);
        out.println();
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
        if (tileColor == TileColor.LIGHT) {
            out.print(SET_BG_COLOR_DARK_GREY);
            tileColor = TileColor.DARK;
        } else {
            out.print(SET_BG_COLOR_LIGHT_GREY);
            tileColor = TileColor.LIGHT;
        }
    }
}
