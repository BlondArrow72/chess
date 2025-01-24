package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements ChessPieceMovesCalculator{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        Collection<ChessMove> possibleMoves = new ArrayList<>();

        int currentRow = myPosition.getRow();
        int currentCol = myPosition.getColumn();

        // knights can move up 2 and right 1
        ChessPosition upUpRightPosition = new ChessPosition(currentRow+2, currentCol+1);
        ChessMove upUpRightMove = new ChessMove(myPosition, upUpRightPosition);
        possibleMoves = evaluateMove(possibleMoves, board, upUpRightMove);

        // knights can move up 1 and right 2
        ChessPosition upRightRightPosition = new ChessPosition(currentRow+1, currentCol+2);
        ChessMove upRightRightMove = new ChessMove(myPosition, upRightRightPosition);
        possibleMoves = evaluateMove(possibleMoves, board, upRightRightMove);

        // knights can move down 1 and right 2
        ChessPosition downRightRightPosition = new ChessPosition(currentRow-1, currentCol+2);
        ChessMove downRightRightMove = new ChessMove(myPosition, downRightRightPosition);
        possibleMoves = evaluateMove(possibleMoves, board, downRightRightMove);

        // knights can move down 2 and right 1
        ChessPosition downDownRightPosition = new ChessPosition(currentRow-2, currentCol+1);
        ChessMove downDownRightMove = new ChessMove(myPosition, downDownRightPosition);
        possibleMoves = evaluateMove(possibleMoves, board, downDownRightMove);

        // knights can move down 2 and left 1
        ChessPosition downDownLeftPosition = new ChessPosition(currentRow-2, currentCol-1);
        ChessMove downDownLeftMove = new ChessMove(myPosition, downDownLeftPosition);
        possibleMoves = evaluateMove(possibleMoves, board, downDownLeftMove);

        // knights can move down 1 and left 2
        ChessPosition downLeftLeftPosition = new ChessPosition(currentRow-1, currentCol-2);
        ChessMove downLeftLeftMove = new ChessMove(myPosition, downLeftLeftPosition);
        possibleMoves = evaluateMove(possibleMoves, board, downLeftLeftMove);

        // knights can move up 1 and left 2
        ChessPosition upLeftLeftPosition = new ChessPosition(currentRow, currentCol-1);
        ChessMove upLeftLeftMove = new ChessMove(myPosition, upLeftLeftPosition);
        possibleMoves = evaluateMove(possibleMoves, board, upLeftLeftMove);

        // knights can move up 2 and left 1
        ChessPosition upUpLeftPosition = new ChessPosition(currentRow+1, currentCol-1);
        ChessMove upUpLeftMove = new ChessMove(myPosition, upUpLeftPosition);
        possibleMoves = evaluateMove(possibleMoves, board, upUpLeftMove);

        return possibleMoves;
    }
}
