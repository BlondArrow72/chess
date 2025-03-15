package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements ChessPieceMovesCalculator{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        Collection<ChessMove> possibleMoves = new ArrayList<>();

        int[][] directions = {
                { 1, -1}, { 1, 1},
                {-1, -1}, {-1, 1}
        };

        for (int[] direction : directions) {
            possibleMoves = continuousMoves(possibleMoves, board, myPosition, direction[0], direction[1]);
        }

        return possibleMoves;
    }
}
