package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame implements Cloneable {

    private ChessBoard board = new ChessBoard();
    private TeamColor teamTurn;

    public ChessGame() {
        board.resetBoard();
        teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessBoard board = getBoard();

        // check if piece at startPosition
        if (board.getPiece(startPosition) == null) {
            return null;
        }

        // get all possible moves
        ChessPiece startPiece = board.getPiece(startPosition);
        Collection<ChessMove> possibleMoves = startPiece.pieceMoves(board, startPosition);

        // Iterate through possible moves, adding them to valid moves if they don't place team in check
        Iterator<ChessMove> chessMoveIterator = possibleMoves.iterator();
        Collection<ChessMove> validMoves = new ArrayList<>();
        while (chessMoveIterator.hasNext()) {
            ChessMove currentMove = chessMoveIterator.next();

            // make each move and confirm King is not in check
            ChessBoard cloneBoard = board.clone();


            chessMoveIterator.remove();
        }

        return possibleMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // Iterate through board
        ChessPosition kingPosition = null;
        Collection<ChessMove> otherTeamMoves = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currentPosition = new ChessPosition(i, j);
                ChessPiece currentPiece = board.getPiece(currentPosition);

                // get king position
                if ((currentPiece.getTeamColor() == teamColor) && (currentPiece.getPieceType() == ChessPiece.PieceType.KING)) {
                    kingPosition = currentPosition;
                }

                // get valid moves for all pieces on other team
                if (currentPiece.getTeamColor() != teamColor) {
                    Collection<ChessMove> currentPieceMoves = currentPiece.pieceMoves(board, currentPosition);

                    otherTeamMoves.addAll(currentPieceMoves);
                }
            }
        }

        // iterate through otherTeamMoves
        Iterator<ChessMove> movesIterator = otherTeamMoves.iterator();
        while (movesIterator.hasNext()) {
            ChessMove currentMove = movesIterator.next();
            ChessPosition targetPosition = currentMove.getEndPosition();

            if (targetPosition == kingPosition) {
                return true;
            }

            movesIterator.remove();
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        board.resetBoard();
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
