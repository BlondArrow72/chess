package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements ChessPieceMovesCalculator{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        Collection<ChessMove> possibleMoves = new ArrayList<>();

        int[][] directions = {
                          { 1, 0},
                { 0, -1},          { 0, 1},
                          {-1, 0}
        };

        for (int[] direction : directions) {
            possibleMoves = continuousMoves(possibleMoves, board, myPosition, direction[0], direction[1]);
        }

        return possibleMoves;
    }
}
