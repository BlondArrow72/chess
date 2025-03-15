package chess;

import java.util.Collection;

/**
 * Interface to calculate piece moves for the game of chess
 * <p>
 *     This interface is implemented by several member classes, one
 *     member class for each chess piece. Member classes have the
 *     naming convention "pieceName"MovesCalculator.
 * </p>
 */
public interface ChessPieceMovesCalculator {
    /**
     *
     * @param board ChessBoard object of the chess game
     * @param myPosition Position of my piece on the chess board
     * @return Collection of possible moves
     */
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);

    default Collection<ChessMove> evaluateMove(Collection<ChessMove> possibleMoves, ChessBoard board, ChessMove newMove) {
        ChessPosition myPosition = newMove.getStartPosition();
        ChessPosition targetPosition = newMove.getEndPosition();

        if (isInBounds(targetPosition) && !isBlocked(board, myPosition, targetPosition)) {
            possibleMoves.add(newMove);
        }

        return possibleMoves;
    }

    default Collection<ChessMove> continuousMoves(Collection<ChessMove> possibleMoves, ChessBoard board, ChessPosition myPosition, int rowDirection, int colDirection) {
        for (int i = 1; i <= 7; i++) {
            int newRow = i * rowDirection + myPosition.getRow();
            int newCol = i * colDirection + myPosition.getColumn();
            ChessPosition newPosition = new ChessPosition(newRow, newCol);
            ChessMove newMove = new ChessMove(myPosition, newPosition);

            if (isInBounds(newPosition)) {
                if (canCapture(board, myPosition, newPosition)) {
                    possibleMoves.add(newMove);
                    break;
                }
                else if (isBlocked(board, myPosition, newPosition)) {
                    break;
                }
                else {
                    possibleMoves.add(newMove);
                }
            }
        }

        return possibleMoves;
    }

    default boolean isInBounds(ChessPosition myPosition) {
        return (myPosition.getRow() >= 1) && (myPosition.getRow() <= 8) && (myPosition.getColumn() >= 1) && (myPosition.getColumn() <= 8);
    }

    default boolean canCapture(ChessBoard board, ChessPosition myPosition, ChessPosition targetPosition) {
        ChessPiece targetPiece = board.getPiece(targetPosition);

        // find other piece if available
        if (targetPiece == null) {
            return false;
        }

        // find team colors
        ChessGame.TeamColor myTeam = board.getPiece(myPosition).getTeamColor();
        ChessGame.TeamColor targetTeam = targetPiece.getTeamColor();

        return myTeam != targetTeam;
    }

    default boolean isBlocked(ChessBoard board, ChessPosition myPosition, ChessPosition targetPosition) {
        ChessPiece targetPiece = board.getPiece(targetPosition);

        // find other piece if available
        if (targetPiece == null) {
            return false;
        }

        // find team colors
        ChessGame.TeamColor myTeam = board.getPiece(myPosition).getTeamColor();
        ChessGame.TeamColor targetTeam = targetPiece.getTeamColor();

        return myTeam == targetTeam;
    }
}
