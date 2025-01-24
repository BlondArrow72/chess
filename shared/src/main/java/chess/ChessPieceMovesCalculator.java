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
