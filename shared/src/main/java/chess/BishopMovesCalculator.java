package chess;

import java.util.ArrayList;

public class BishopMovesCalculator implements ChessPieceMovesCalculator{
    public int[][] pieceMoves(ChessBoard board, ChessPosition myPosition) {

        ArrayList<int[]> possibleMoves = new ArrayList<>();

        for (int i = 1; i < 8; i++) {
            // bishops can move diagonally
            int[] upRight = {myPosition.getRow() + i, myPosition.getColumn() + i};
            if (isInBounds(myPosition)) {
                possibleMoves.add(upRight);
            }

            int[] upLeft = {myPosition.getRow() + i, myPosition.getColumn() - i};
            if (isInBounds(myPosition)) {
                possibleMoves.add(upLeft);
            }

            int[] downRight = {myPosition.getRow() - i, myPosition.getColumn() + i};
            if (isInBounds(myPosition)) {
                possibleMoves.add(downRight);
            }

            int[] downLeft = {myPosition.getRow() - i, myPosition.getColumn() - i};
            if (isInBounds(myPosition)) {
                possibleMoves.add(downLeft);
            }
        }

        return possibleMoves.toArray(new int[0][]);
    }

    public boolean isInBounds(ChessPosition myPosition) {
        return myPosition.getRow() >= 1 && myPosition.getRow() <= 8 && myPosition.getColumn() >= 1 && myPosition.getColumn() <= 8;
    }
}
