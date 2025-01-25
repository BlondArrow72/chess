package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements ChessPieceMovesCalculator{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        Collection<ChessMove> possibleMoves = new ArrayList<>();

        int currentRow = myPosition.getRow();
        int currentCol = myPosition.getColumn();

        // pawns can move up
        ChessPosition upPosition = new ChessPosition(currentRow+1, currentCol);
        ChessMove upMove = new ChessMove(myPosition, upPosition);

        if (isInBounds(upPosition) && !isBlocked(board, myPosition, upPosition) && !canCapture(board, myPosition, upPosition)) {
            possibleMoves.add(upMove);
        }

        // pawns can capture up and to the right
        ChessPosition upRightPosition = new ChessPosition(currentRow+1, currentCol+1);
        ChessMove upRightMove = new ChessMove(myPosition, upRightPosition);

        if (isInBounds(upRightPosition) && !isBlocked(board, myPosition, upRightPosition) && canCapture(board, myPosition, upRightPosition)) {
            possibleMoves.add(upRightMove);
        }

        // pawns can capture up and to the left
        ChessPosition upLeftPosition = new ChessPosition(currentRow+1, currentCol-1);
        ChessMove upLeftMove = new ChessMove(myPosition, upLeftPosition);

        if (isInBounds(upLeftPosition) && !isBlocked(board, myPosition, upLeftPosition) && canCapture(board, myPosition, upLeftPosition)) {
            possibleMoves.add(upLeftMove);
        }

        return possibleMoves;
    }
}
