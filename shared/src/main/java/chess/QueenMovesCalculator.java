package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements ChessPieceMovesCalculator{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        Collection<ChessMove> possibleMoves = new ArrayList<>();


        int currentRow = myPosition.getRow();
        int currentCol = myPosition.getColumn();

        // queens can move up
        for (int i = 1; i <= 7; i++) {
            ChessPosition up = new ChessPosition(currentRow+i, currentCol);
            ChessMove newMove = new ChessMove(myPosition, up);

            if (isInBounds(up)) {
                if (canCapture(board, myPosition, up)) {
                    possibleMoves.add(newMove);
                    break;
                }
                else if (isBlocked(board, myPosition, up)) {
                    break;
                }
                else {
                    possibleMoves.add(newMove);
                }
            }
        }

        // queens can move up and to the right
        for (int i = 1; i <= 7; i++) {
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

        // queens can move to the right
        for (int i = 1; i <= 7; i++) {
            ChessPosition right = new ChessPosition(currentRow, currentCol+i);
            ChessMove newMove = new ChessMove(myPosition, right);

            if (isInBounds(right)) {
                if (canCapture(board, myPosition, right)) {
                    possibleMoves.add(newMove);
                    break;
                }
                else if (isBlocked(board, myPosition, right)) {
                    break;
                }
                else {
                    possibleMoves.add(newMove);
                }
            }
        }

        // queens can move down and to the right
        for (int i = 1; i <= 7; i++) {
            ChessPosition downRight = new ChessPosition(currentRow-i, currentCol+i);
            ChessMove newMove = new ChessMove(myPosition, downRight);

            if (isInBounds(downRight)) {
                if (canCapture(board, myPosition, downRight)) {
                    possibleMoves.add(newMove);
                    break;
                }
                else if (isBlocked(board, myPosition, downRight)) {
                    break;
                }
                else {
                    possibleMoves.add(newMove);
                }
            }
        }

        // queens can move down
        for (int i = 1; i <= 7; i++) {
            ChessPosition down = new ChessPosition(currentRow-i, currentCol);
            ChessMove newMove = new ChessMove(myPosition, down);

            if (isInBounds(down)) {
                if (canCapture(board, myPosition, down)) {
                    possibleMoves.add(newMove);
                    break;
                }
                else if (isBlocked(board, myPosition, down)) {
                    break;
                }
                else {
                    possibleMoves.add(newMove);
                }
            }
        }

        // queens can move down and to the left
        for (int i = 1; i <= 7; i++) {
            ChessPosition downLeft = new ChessPosition(currentRow-i, currentCol-i);
            ChessMove newMove = new ChessMove(myPosition, downLeft);

            if (isInBounds(downLeft)) {
                if (canCapture(board, myPosition, downLeft)) {
                    possibleMoves.add(newMove);
                    break;
                }
                else if (isBlocked(board, myPosition, downLeft)) {
                    break;
                }
                else {
                    possibleMoves.add(newMove);
                }
            }
        }

        // queens can move to the left
        for (int i = 1; i <= 7; i++) {
            ChessPosition left = new ChessPosition(currentRow, currentCol-i);
            ChessMove newMove = new ChessMove(myPosition, left);

            if (isInBounds(left)) {
                if (canCapture(board, myPosition, left)) {
                    possibleMoves.add(newMove);
                    break;
                }
                else if (isBlocked(board, myPosition, left)) {
                    break;
                }
                else {
                    possibleMoves.add(newMove);
                }
            }
        }

        // queens can move up and to the left
        for (int i = 1; i <= 7; i++) {
            ChessPosition upLeft = new ChessPosition(currentRow+i, currentCol-i);
            ChessMove newMove = new ChessMove(myPosition, upLeft);

            if (isInBounds(upLeft)) {
                if (canCapture(board, myPosition, upLeft)) {
                    possibleMoves.add(newMove);
                    break;
                }
                else if (isBlocked(board, myPosition, upLeft)) {
                    break;
                }
                else {
                    possibleMoves.add(newMove);
                }
            }
        }

        return possibleMoves;
    }
}
