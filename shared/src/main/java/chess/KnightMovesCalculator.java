package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements ChessPieceMovesCalculator{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        Collection<ChessMove> possibleMoves = new ArrayList<>();

        int[][] directions = {
                          { 2, -1}, { 2, 1},
                { 1, -2},                    { 1, 2},
                {-1, -2},                    {-1, 2},
                          {-2, -1}, {-2, 1}
        };

        for (int[] direction : directions) {
            int newRow = direction[0] + myPosition.getRow();
            int newCol = direction[1] + myPosition.getColumn();
            ChessPosition newPosition = new ChessPosition(newRow, newCol);
            ChessMove newMove = new ChessMove(myPosition, newPosition);
            possibleMoves = evaluateMove(possibleMoves, board, newMove);
        }

        return possibleMoves;
    }
}
