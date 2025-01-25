package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements ChessPieceMovesCalculator{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        Collection<ChessMove> possibleMoves = new ArrayList<>();

        int currentRow = myPosition.getRow();
        int currentCol = myPosition.getColumn();

        ChessPiece myPiece = board.getPiece(myPosition);
        ChessGame.TeamColor myTeam = myPiece.getTeamColor();

        // pawns can move up if white
        ChessPosition upPosition = new ChessPosition(currentRow+1, currentCol);
        ChessMove upMove = new ChessMove(myPosition, upPosition);

        if (isInBounds(upPosition) && !isBlocked(board, myPosition, upPosition) && !canCapture(board, myPosition, upPosition) && (myTeam == ChessGame.TeamColor.WHITE)) {
            possibleMoves.add(upMove);
        }

        // pawns can move down if black
        ChessPosition downPosition = new ChessPosition(currentRow-1, currentCol);
        ChessMove downMove = new ChessMove(myPosition, downPosition);

        if (isInBounds(downPosition) && !isBlocked(board, myPosition, downPosition) && !canCapture(board, myPosition, downPosition) && (myTeam == ChessGame.TeamColor.BLACK)) {
            possibleMoves.add(downMove);
        }

        // pawns can capture up and to the right if white
        ChessPosition upRightPosition = new ChessPosition(currentRow+1, currentCol+1);
        ChessMove upRightMove = new ChessMove(myPosition, upRightPosition);

        if (isInBounds(upRightPosition) && !isBlocked(board, myPosition, upRightPosition) && canCapture(board, myPosition, upRightPosition) && (myTeam == ChessGame.TeamColor.WHITE)) {
            possibleMoves.add(upRightMove);
        }

        // pawns can capture down and to the right if black
        ChessPosition downRightPosition = new ChessPosition(currentRow-1, currentCol+1);
        ChessMove downRightMove = new ChessMove(myPosition, downRightPosition);

        if (isInBounds(downRightPosition) && !isBlocked(board, myPosition, downRightPosition) && canCapture(board, myPosition, downRightPosition) && (myTeam == ChessGame.TeamColor.BLACK)) {
            possibleMoves.add(downRightMove);
        }

        // pawns can capture up and to the left if white
        ChessPosition upLeftPosition = new ChessPosition(currentRow+1, currentCol-1);
        ChessMove upLeftMove = new ChessMove(myPosition, upLeftPosition);

        if (isInBounds(upLeftPosition) && !isBlocked(board, myPosition, upLeftPosition) && canCapture(board, myPosition, upLeftPosition) && (myTeam == ChessGame.TeamColor.WHITE)) {
            possibleMoves.add(upLeftMove);
        }

        // pawns can capture down and to the left if black
        ChessPosition downLeftPosition = new ChessPosition(currentRow-1, currentCol-1);
        ChessMove downLeftMove = new ChessMove(myPosition, downLeftPosition);

        if (isInBounds(downLeftPosition) && !isBlocked(board, myPosition, downLeftPosition) && canCapture(board, myPosition, downLeftPosition) && (myTeam == ChessGame.TeamColor.BLACK)) {
            possibleMoves.add(downLeftMove);
        }

        return possibleMoves;
    }
}
