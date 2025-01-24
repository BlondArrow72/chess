package chess;

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
     * @param board
     * @param myPosition
     * @return
     */
    public int[][] pieceMoves(ChessBoard board, ChessPosition myPosition);

    public boolean isInBounds(ChessPosition myPosition);
}
