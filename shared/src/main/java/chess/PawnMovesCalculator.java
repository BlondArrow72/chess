package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Arrays;

public class PawnMovesCalculator implements ChessPieceMovesCalculator{
    private ChessBoard board;
    private ChessPosition myPosition;

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;

        Collection<ChessMove> possibleMoves = new ArrayList<>();

        int currentRow = myPosition.getRow();
        int currentCol = myPosition.getColumn();

        ChessPiece myPiece = board.getPiece(myPosition);
        ChessGame.TeamColor myTeam = myPiece.getTeamColor();

        // pawns can move up if white
        ChessPosition upPosition = new ChessPosition(currentRow+1, currentCol);
        ChessMove upMove = new ChessMove(myPosition, upPosition);

        if (isInBounds(upPosition) && !isBlocked(board, myPosition, upPosition) && !canCapture(board, myPosition, upPosition) && (myTeam == ChessGame.TeamColor.WHITE)) {
            if (currentRow==7) {
                possibleMoves.addAll(getPromotionMoves(upPosition));
            }
            else {
                possibleMoves.add(upMove);
            }
        }

        // pawns can move up twice if white and second row
        ChessPosition upUpPosition = new ChessPosition(currentRow+2, currentCol);
        ChessMove upUpMove = new ChessMove(myPosition, upUpPosition);

        if (isInBounds(upUpPosition) && !isBlocked(board, myPosition, upPosition) && !isBlocked(board, myPosition, upUpPosition) && !canCapture(board, myPosition, upUpPosition) && (myTeam == ChessGame.TeamColor.WHITE) && (currentRow == 2)) {
            possibleMoves.add(upUpMove);
        }

        // pawns can move down if black
        ChessPosition downPosition = new ChessPosition(currentRow-1, currentCol);
        ChessMove downMove = new ChessMove(myPosition, downPosition);

        if (isInBounds(downPosition) && !isBlocked(board, myPosition, downPosition) && !canCapture(board, myPosition, downPosition) && (myTeam == ChessGame.TeamColor.BLACK)) {
            if (currentRow==2) {
                possibleMoves.addAll(getPromotionMoves(downPosition));
            }
            else {
                possibleMoves.add(downMove);
            }
        }

        // pawns can move down twice if black and seventh row
        ChessPosition downDownPosition = new ChessPosition(currentRow-2, currentCol);
        ChessMove downDownMove = new ChessMove(myPosition, downDownPosition);

        if (isInBounds(downDownPosition) && !isBlocked(board, myPosition, downPosition) && !isBlocked(board, myPosition, downDownPosition) && !canCapture(board, myPosition, downDownPosition) && (myTeam == ChessGame.TeamColor.BLACK) && (currentRow == 7)) {
            possibleMoves.add(downDownMove);
        }

        // pawns can capture up and to the right if white
        ChessPosition upRightPosition = new ChessPosition(currentRow+1, currentCol+1);
        ChessMove upRightMove = new ChessMove(myPosition, upRightPosition);

        if (isInBounds(upRightPosition) && !isBlocked(board, myPosition, upRightPosition) && canCapture(board, myPosition, upRightPosition) && (myTeam == ChessGame.TeamColor.WHITE)) {
            if (currentRow==7) {
                possibleMoves.addAll(getPromotionMoves(upRightPosition));
            }
            else {
                possibleMoves.add(upRightMove);
            }
        }

        // pawns can capture down and to the right if black
        ChessPosition downRightPosition = new ChessPosition(currentRow-1, currentCol+1);
        ChessMove downRightMove = new ChessMove(myPosition, downRightPosition);

        if (isInBounds(downRightPosition) && !isBlocked(board, myPosition, downRightPosition) && canCapture(board, myPosition, downRightPosition) && (myTeam == ChessGame.TeamColor.BLACK)) {
            if (currentRow==2) {
                possibleMoves.addAll(getPromotionMoves(downRightPosition));
            }
            else {
                possibleMoves.add(downRightMove);
            }
        }

        // pawns can capture up and to the left if white
        ChessPosition upLeftPosition = new ChessPosition(currentRow+1, currentCol-1);
        ChessMove upLeftMove = new ChessMove(myPosition, upLeftPosition);

        if (isInBounds(upLeftPosition) && !isBlocked(board, myPosition, upLeftPosition) && canCapture(board, myPosition, upLeftPosition) && (myTeam == ChessGame.TeamColor.WHITE)) {
            if (currentRow==7) {
                possibleMoves.addAll(getPromotionMoves(upLeftPosition));
            }
            else {
                possibleMoves.add(upLeftMove);
            }
        }

        // pawns can capture down and to the left if black
        ChessPosition downLeftPosition = new ChessPosition(currentRow-1, currentCol-1);
        ChessMove downLeftMove = new ChessMove(myPosition, downLeftPosition);

        if (isInBounds(downLeftPosition) && !isBlocked(board, myPosition, downLeftPosition) && canCapture(board, myPosition, downLeftPosition) && (myTeam == ChessGame.TeamColor.BLACK)) {
            if (currentRow==2) {
                possibleMoves.addAll(getPromotionMoves(downLeftPosition));
            }
            else {
                possibleMoves.add(downLeftMove);
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
