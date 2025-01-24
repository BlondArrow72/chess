package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements ChessPieceMovesCalculator{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        Collection<ChessMove> possibleMoves = new ArrayList<>();

        int currentRow = myPosition.getRow();
        int currentCol = myPosition.getColumn();

        // bishops can move up and to the right
        for (int i = 1; i < 7; i++) {
            ChessPosition upRight = new ChessPosition(currentRow+i, currentCol+i);
            ChessMove newMove = new ChessMove(myPosition, upRight);

            if (isInBounds(upRight)) {
                if (canCapture(board, myPosition, upRight)) {
                    possibleMoves.add(newMove);
                    break;
                }
                else if (isBlocked(board, myPosition, upRight)) {
                    break;
                }
                else {
                    possibleMoves.add(newMove);
                }
            }
        }

        // bishops can move up and to the left
        for (int i = 1; i < 7; i++) {
            ChessPosition upLeft = new ChessPosition(currentRow+i, currentCol-i);
            if (isInBounds(upLeft)) {
                ChessMove newMove = new ChessMove(myPosition, upLeft);
                possibleMoves.add(newMove);
            }
        }

        // bishops can move down and to the right
        for (int i = 1; i < 7; i++) {
            ChessPosition downRight = new ChessPosition(currentRow-i, currentCol+i);
            if (isInBounds(downRight)) {
                ChessMove newMove = new ChessMove(myPosition, downRight);
                possibleMoves.add(newMove);
            }
        }

        // bishops can move down and to the left
        for (int i = 1; i < 7; i++) {
            ChessPosition downLeft = new ChessPosition(currentRow-i, currentCol-i);
            if (isInBounds(downLeft)) {
                ChessMove newMove = new ChessMove(myPosition, downLeft);
                possibleMoves.add(newMove);
            }
        }

        return possibleMoves;
    }
}
