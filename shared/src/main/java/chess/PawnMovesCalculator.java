package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Arrays;

public class PawnMovesCalculator implements ChessPieceMovesCalculator{
    ChessBoard board;
    ChessPosition myPosition;
    ChessGame.TeamColor teamColor;

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;

        ChessPiece currentPiece = board.getPiece(myPosition);
        if (currentPiece == null) {
            return null;
        }

        this.teamColor = board.getPiece(myPosition).getTeamColor();

        int currentRow = myPosition.getRow();
        int currentCol = myPosition.getColumn();

        // pawns can make regular moves
        ChessPosition newPosition;
        if (teamColor == ChessGame.TeamColor.WHITE) {
            newPosition = new ChessPosition(currentRow + 1, currentCol);
        }
        else {
            newPosition = new ChessPosition(currentRow - 1, currentCol);
        }
        Collection<ChessMove> possibleMoves = regularMove(newPosition);

        // pawns can move twice from base row
        ChessPosition enPassantPosition = null;
        if (teamColor == ChessGame.TeamColor.WHITE && myPosition.getRow() == 2
                && board.getPiece(new ChessPosition(currentRow + 1, currentCol)) == null) {
            enPassantPosition = new ChessPosition(currentRow + 2, currentCol);
        }
        if (teamColor == ChessGame.TeamColor.BLACK && myPosition.getRow() == 7
                && board.getPiece(new ChessPosition(currentRow - 1, currentCol)) == null) {
            enPassantPosition = new ChessPosition(currentRow - 2, currentCol);
        }
        if (enPassantPosition != null) {
            possibleMoves.addAll(regularMove(enPassantPosition));
        }

        // pawns can capture
        Collection<ChessPosition> capturePositions = new ArrayList<>();
        if (teamColor == ChessGame.TeamColor.WHITE) {
            capturePositions.add(new ChessPosition(currentRow + 1, currentCol - 1));
            capturePositions.add(new ChessPosition(currentRow + 1, currentCol + 1));
        }
        else {
            capturePositions.add(new ChessPosition(currentRow - 1, currentCol - 1));
            capturePositions.add(new ChessPosition(currentRow - 1, currentCol + 1));
        }
        for (ChessPosition capturePosition : capturePositions) {
            possibleMoves.addAll(captureMove(capturePosition));
        }

        return possibleMoves;
    }

    private Collection<ChessMove> regularMove(ChessPosition newPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();

        // move must be in bounds and unoccupied
        if (isInBounds(newPosition) && board.getPiece(newPosition) == null) {
            // if white and newPosition is row 8
            if (teamColor == ChessGame.TeamColor.WHITE) {
                // get promotionMoves
                if (newPosition.getRow() == 8) {
                    possibleMoves.addAll(getPromotionMoves(newPosition));
                }

                // get regular moves
                else {
                    ChessMove newMove = new ChessMove(myPosition, newPosition);
                    possibleMoves = evaluateMove(possibleMoves, board, newMove);
                }
            }

            // if black
            else {
                // get promotion moves
                if (newPosition.getRow() == 1) {
                    possibleMoves.addAll(getPromotionMoves(newPosition));
                }

                // get regular moves
                else {
                    ChessMove newMove = new ChessMove(myPosition, newPosition);
                    possibleMoves = evaluateMove(possibleMoves, board, newMove);
                }
            }
        }

        return possibleMoves;
    }

    private Collection<ChessMove> captureMove(ChessPosition newPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();

        // capture position must be in bounds, not blocked by your team, and occupied by other team
        if (isInBounds(newPosition)) {
            ChessPiece targetPiece = board.getPiece(newPosition);
            if (targetPiece != null && teamColor != targetPiece.getTeamColor()) {

                // promote if white on last row
                if (teamColor == ChessGame.TeamColor.WHITE && newPosition.getRow() == 8) {
                    possibleMoves.addAll(getPromotionMoves(newPosition));
                }

                // promote if black on first row
                else if (teamColor == ChessGame.TeamColor.BLACK && newPosition.getRow() == 1) {
                    possibleMoves.addAll(getPromotionMoves(newPosition));
                }

                // add move if regular capture
                else {
                    ChessMove newMove = new ChessMove(myPosition, newPosition);
                    possibleMoves.add(newMove);
                }
            }
        }

        return possibleMoves;
    }

    private Collection<ChessMove> getPromotionMoves(ChessPosition promotionPosition) {
        // instantiate promotionMoves
        Collection<ChessMove> promotionMoves = new ArrayList<>();

        // promotion types
        Collection<ChessPiece.PieceType> promotionTypes = Arrays.asList(
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.QUEEN
        );

        // add all move types to promotionMoves
        for(ChessPiece.PieceType pieceType : promotionTypes) {
            ChessMove newMove = new ChessMove(myPosition, promotionPosition, pieceType);
            promotionMoves.add(newMove);
        }

        return promotionMoves;
    }
}
