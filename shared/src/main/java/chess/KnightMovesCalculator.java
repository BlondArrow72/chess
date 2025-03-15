package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements ChessPieceMovesCalculator{

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int[][] directions = {
                          { 2, -1}, { 2, 1},
                { 1, -2},                    { 1, 2},
                {-1, -2},                    {-1, 2},
                          {-2, -1}, {-2, 1}
        };

        return getDiscreteMoves(directions, board, myPosition);
    }
}
