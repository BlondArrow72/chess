package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

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

            // get piece from start position
            ChessPiece movePiece = cloneBoard.getPiece(currentMove.getStartPosition());

            // place piece at end position
            cloneBoard.addPiece(currentMove.getEndPosition(), movePiece);

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
        ChessPosition kingPosition = getKingPosition(teamColor);

        // Get all possible moves on board and King's position
        Collection<ChessMove> otherTeamMoves = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currentPosition = new ChessPosition(i, j);
                ChessPiece currentPiece = board.getPiece(currentPosition);

                if (currentPiece == null) {
                    continue;
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

            if (targetPosition.equals(kingPosition)) {
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
        // make sure King is currently in check
        if (!isInCheck(teamColor)) {
            return false;
        }

        // get King position
        ChessPosition kingPosition = getKingPosition(teamColor);

        // get King piece
        ChessPiece kingPiece = board.getPiece(kingPosition);

        // get King possibleMoves
        Collection<ChessMove> kingMoves = kingPiece.pieceMoves(board, kingPosition);

        for (ChessMove currentMove : kingMoves) {
            ChessGame cloneGame = clone();

            // Make move in cloned game
            ChessPosition newPosition = currentMove.getEndPosition();
            cloneGame.board.addPiece(newPosition, kingPiece);
            cloneGame.board.addPiece(kingPosition, null);

            if (!cloneGame.isInCheck(teamColor)) {
                return false;
            }
        }

        // check if capture will escape checkmate
        TeamColor otherTeam;
        if (teamColor == TeamColor.WHITE) {
            otherTeam = TeamColor.BLACK;
        }
        else {
            otherTeam = TeamColor.WHITE;
        }

        // get all moves that attack King
        Collection<ChessMove> otherTeamMoves = getTeamMoves(otherTeam);
        Collection<ChessMove> attackMoves = new ArrayList<>();
        for (ChessMove currentMove : otherTeamMoves) {
            ChessPosition attackingPosition = currentMove.getEndPosition();
            if (attackingPosition.equals(kingPosition)) {
                attackMoves.add(currentMove);
            }
        }

        // get all moves from my team
        Collection<ChessMove> myTeamMoves = getTeamMoves(teamColor);

        // get all startingAttackPositions
        Collection<ChessPosition> attackPositions = new ArrayList<>();
        for (ChessMove currentMove : attackMoves) {
            ChessPosition currentStartPosition = currentMove.getStartPosition();
            if (!attackPositions.contains(currentStartPosition)) {
                attackPositions.add(currentStartPosition);
            }
        }

        // Iterate through each attacking move
        Iterator<ChessPosition> attackingPositions = attackPositions.iterator();
        while (attackingPositions.hasNext()) {
            ChessPosition attackPiecePosition = attackingPositions.next();

            // iterate through myTeamMoves to see if I can make a move to capture the attacking piece
            for (ChessMove defendingMove : myTeamMoves) {
                ChessPosition defendingMoveEndPosition = defendingMove.getEndPosition();

                // if my defending move can capture the attacking piece, might not be in check
                if (defendingMoveEndPosition.equals(attackPiecePosition)) {
                    if (attackingPositions.hasNext()) {
                        return true;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        // King must be safe
        if (isInCheck(teamColor)) {
            return false;
        }

        // King must not be able to move
        ChessPosition kingPosition = getKingPosition(teamColor);
        Collection<ChessMove> kingMoves = board.getPiece(kingPosition).pieceMoves(board, kingPosition);
        for (ChessMove move : kingMoves) {
            ChessGame cloneGame = clone();
            ChessPosition newKingPosition = move.getEndPosition();
            cloneGame.board.addPiece(newKingPosition, new ChessPiece(teamColor, ChessPiece.PieceType.KING));
            cloneGame.board.addPiece(kingPosition, null);

            if (isInCheck(teamColor)) {
                return false;
            }
        }

        // team must have no moves
        Collection<ChessMove> teamMoves = getTeamMoves(teamColor);
        Iterator<ChessMove> teamMovesIterator = teamMoves.iterator();
        while (teamMovesIterator.hasNext()) {
            ChessMove nextMove = teamMovesIterator.next();
            if (nextMove.getStartPosition().equals(kingPosition)) {
                teamMovesIterator.remove();
            }
        }

        if (teamMoves.isEmpty()) {
            return true;
        }

        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    /**
     * Gets the King's position
     *
     * @param teamColor which team's King we want to find
     * @return ChessPosition position of King on ChessBoard
     */
    private ChessPosition getKingPosition(ChessGame.TeamColor teamColor) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currentPosition = new ChessPosition(i, j);
                ChessPiece currentPiece = board.getPiece(currentPosition);

                if (currentPiece == null) {
                    continue;
                }

                if ((currentPiece.pieceColor == teamColor) && (currentPiece.type == ChessPiece.PieceType.KING)) {
                    return currentPosition;
                }
            }
        }
        return null;
    }

    /**
     * Gets all moves for the team color
     *
     * @param teamColor which team's moves to return
     * @return Collection<ChessMove> all moves possible for the team
     */
     private Collection<ChessMove> getTeamMoves(ChessGame.TeamColor teamColor){
         Collection<ChessMove> teamMoves = new ArrayList<>();
         for (int i = 1; i <= 8; i++) {
             for (int j = 1; j <= 8; j++) {
                 ChessPosition currentPosition = new ChessPosition(i, j);
                 ChessPiece currentPiece = board.getPiece(currentPosition);

                 if (currentPiece == null) {
                     continue;
                 }

                 if (currentPiece.getTeamColor() != teamColor) {
                     continue;
                 }

                 teamMoves.addAll(currentPiece.pieceMoves(board, currentPosition));
             }
         }
         return teamMoves;
    }

    @Override
    public ChessGame clone() {
        try {
            ChessGame cloneGame = (ChessGame) super.clone();
            cloneGame.board = board.clone();

            return cloneGame;
        }
        catch (CloneNotSupportedException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "board=" + board +
                ", teamTurn=" + teamTurn +
                '}';
    }
}
