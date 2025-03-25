package ui;

import chess.ChessBoard;
import chess.ChessGame;

import model.JoinGameRequest;

public class GameplayUI {
    public void run(JoinGameRequest joinGameRequest) {
        boolean reverse = joinGameRequest.playerColor() == ChessGame.TeamColor.BLACK;

        UiChessBoard uiChessBoard = new UiChessBoard();
        ChessBoard defaultBoard = new ChessBoard();
        defaultBoard.resetBoard();

        uiChessBoard.drawBoard(defaultBoard, reverse);
    }
}
