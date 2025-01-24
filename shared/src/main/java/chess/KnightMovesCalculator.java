package chess;

import java.util.ArrayList;

public class KnightMovesCalculator implements ChessPieceMovesCalculator {
    public int[][] pieceMoves(ChessBoard board, ChessPosition myPosition) {

        ArrayList<int[]> possibleMoves = new ArrayList<>();

        for (int i = 1; i < 8; i++) {
            // bishops can move diagonally
            int[] upRight = {myPosition.getRow() + i, myPosition.getColumn() + i};
            if (upRight[0] <= 8 && upRight[1] <= 8) {
                possibleMoves.add(upRight);
            }

            int[] upLeft = {myPosition.getRow() + i, myPosition.getColumn() - i};
            if (upRight[0] <= 8 && upRight[1] >= 1) {
                possibleMoves.add(upLeft);
            }

            int[] downRight = {myPosition.getRow() - i, myPosition.getColumn() + i};
            if (upRight[0] >= 1 && upRight[1] <= 8) {
                possibleMoves.add(downRight);
            }

            int[] downLeft = {myPosition.getRow() - i, myPosition.getColumn() - i};
            if (upRight[0] >= 1 && upRight[1] >= 1) {
                possibleMoves.add(downLeft);
            }
        }

        return possibleMoves.toArray(new int[0][]);
    }

    public boolean isInBounds(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }
}
