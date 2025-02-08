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
        // if no piece at location, return null
        if (startPosition == null) {
            return null;
        }

        // get empty validMoves
        Collection<ChessMove> validMoves = new ArrayList<>();

        // get piece at start location
        ChessPiece startPiece = board.getPiece(startPosition);
        ChessGame.TeamColor pieceTeam = startPiece.getTeamColor();

        // get all possible moves for the piece
        Collection<ChessMove> possibleMoves = startPiece.pieceMoves(board, startPosition);

        // if King is in check, block it
        if (isInCheck(pieceTeam)) {
            // loop through possibleMoves and see if it blocks check
            for (ChessMove currentMove : possibleMoves) {
                ChessPosition endPosition = currentMove.getEndPosition();
                ChessPiece.PieceType promotionType = currentMove.getPromotionPiece();
                ChessPiece newPiece;

                if (promotionType != null) {
                    newPiece = new ChessPiece(pieceTeam, promotionType);
                }
                else {
                    newPiece = startPiece;
                }

                // clone game and make move
                ChessGame cloneGame = clone();
                cloneGame.board.addPiece(endPosition, newPiece);
                cloneGame.board.addPiece(startPosition, null);

                // if move saved from check, add to valid moves
                if (!cloneGame.isInCheck(pieceTeam)) {
                    validMoves.add(currentMove);
                }
            }
            return validMoves;
        }

        // if King is not in check, add moves that don't put it in check
        for (ChessMove currentMove : possibleMoves) {
            ChessPosition endPosition = currentMove.getEndPosition();
            ChessPiece.PieceType promotionPiece = currentMove.getPromotionPiece();

            ChessPiece newPiece;
            if (promotionPiece != null) {
                newPiece = new ChessPiece(startPiece.getTeamColor(), promotionPiece);
            } else {
                newPiece = startPiece;
            }

            // clone board
            ChessGame cloneGame = clone();

            // make the move
            cloneGame.board.addPiece(endPosition, newPiece);
            cloneGame.board.addPiece(startPosition, null);

            // if King is not in check, add move
            if (!cloneGame.isInCheck(startPiece.getTeamColor())) {
                validMoves.add(currentMove);
            }
        }

        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece.PieceType promotionType = move.getPromotionPiece();

        // check to see if there is a piece to be moved
        if (board.getPiece(startPosition) == null) {
            throw new InvalidMoveException("No piece at starting position.");
        }

        // ensure move is valid
        Collection<ChessMove> currentValidMoves = validMoves(startPosition);
        if (!currentValidMoves.contains(move)) {
            throw new InvalidMoveException("Invalid move. Try a different move.");
        }

        // get current piece
        ChessPiece chessPiece = board.getPiece(startPosition);
        ChessGame.TeamColor pieceColor = chessPiece.pieceColor;

        // ensure correct team is moving
        if (teamTurn != pieceColor) {
            throw new InvalidMoveException("Other team's turn, please wait.");
        }

        // get new piece
        ChessPiece newPiece;
        if (promotionType != null) {
            newPiece = new ChessPiece(pieceColor, promotionType);
        }
        else {
            newPiece = chessPiece;
        }

        // make the move
        board.addPiece(endPosition, newPiece);
        board.addPiece(startPosition, null);

        // change turn
        if (teamTurn == TeamColor.WHITE) {
            teamTurn = TeamColor.BLACK;
        }
        else {
            teamTurn = TeamColor.WHITE;
        }
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

        // if King can move anywhere and not be in check, not in checkmate
        Collection<ChessMove> validKingMoves = validMoves(kingPosition);
        if (!validKingMoves.isEmpty()) {
            return false;
        }

        // if piece can capture or block, then not in checkmate
        Collection<ChessMove> validTeamMoves = getTeamMoves(teamColor);
        Collection<ChessMove> safeTeamMoves = new ArrayList<>();
        for (ChessMove validMove : validTeamMoves) {
            // get a cloned game
            ChessGame cloneGame = clone();

            // make the move
            try {
                cloneGame.makeMove(validMove);
                if (!cloneGame.isInCheck(teamColor)) {
                    return false;
                }
            } catch (InvalidMoveException e) {

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

                 teamMoves.addAll(validMoves(currentPosition));
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
