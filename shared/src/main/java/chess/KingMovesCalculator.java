package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements ChessPieceMovesCalculator{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        Collection<ChessMove> possibleMoves = new ArrayList<>();

        int currentRow = myPosition.getRow();
        int currentCol = myPosition.getColumn();

        // kings can move up
        ChessPosition upPosition = new ChessPosition(currentRow+1, currentCol);
        ChessMove upMove = new ChessMove(myPosition, upPosition);
        possibleMoves = evaluateMove(possibleMoves, board, upMove);

        // kings can move up and to the right
        ChessPosition upRightPosition = new ChessPosition(currentRow+1, currentCol+1);
        ChessMove upRightMove = new ChessMove(myPosition, upRightPosition);
        possibleMoves = evaluateMove(possibleMoves, board, upRightMove);

        // kings can move to the right
        ChessPosition rightPosition = new ChessPosition(currentRow, currentCol+1);
        ChessMove rightMove = new ChessMove(myPosition, rightPosition);
        possibleMoves = evaluateMove(possibleMoves, board, rightMove);

        // kings can move down and to the right
        ChessPosition downRightPosition = new ChessPosition(currentRow-1, currentCol+1);
        ChessMove downRightMove = new ChessMove(myPosition, downRightPosition);
        possibleMoves = evaluateMove(possibleMoves, board, downRightMove);

        // kings can move down
        ChessPosition downPosition = new ChessPosition(currentRow-1, currentCol);
        ChessMove downMove = new ChessMove(myPosition, downPosition);
        possibleMoves = evaluateMove(possibleMoves, board, downMove);

        // kings can move down and to the left
        ChessPosition downLeftPosition = new ChessPosition(currentRow-1, currentCol-1);
        ChessMove downLeftMove = new ChessMove(myPosition, downLeftPosition);
        possibleMoves = evaluateMove(possibleMoves, board, downLeftMove);

        // kings can move to the left
        ChessPosition leftPosition = new ChessPosition(currentRow, currentCol-1);
        ChessMove leftMove = new ChessMove(myPosition, leftPosition);
        possibleMoves = evaluateMove(possibleMoves, board, leftMove);

        // kings can move up and to the left
        ChessPosition upLeftPosition = new ChessPosition(currentRow+1, currentCol-1);
        ChessMove upLeftMove = new ChessMove(myPosition, upLeftPosition);
        possibleMoves = evaluateMove(possibleMoves, board, upLeftMove);

        return possibleMoves;
    }
}
