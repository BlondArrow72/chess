package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements ChessPieceMovesCalculator{

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int[][] directions = {
                { 1, -1}, { 1, 0}, { 1, 1},
                { 0, -1},          { 0, 1},
                {-1, -1}, {-1, 0}, {-1, 1}
        };

        return getDiscreteMoves(directions, board, myPosition);
    }
}
