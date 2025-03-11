package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
            if (currentRow==7) {
                ChessMove bishopPromotionMove = new ChessMove(myPosition, upPosition, ChessPiece.PieceType.BISHOP);
                possibleMoves.add(bishopPromotionMove);

                ChessMove knightPromotionMove = new ChessMove(myPosition, upPosition, ChessPiece.PieceType.KNIGHT);
                possibleMoves.add(knightPromotionMove);

                ChessMove rookPromotionMove = new ChessMove(myPosition, upPosition, ChessPiece.PieceType.ROOK);
                possibleMoves.add(rookPromotionMove);

                ChessMove queenPromotionMove = new ChessMove(myPosition, upPosition, ChessPiece.PieceType.QUEEN);
                possibleMoves.add(queenPromotionMove);
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
                ChessMove bishopPromotionMove = new ChessMove(myPosition, downPosition, ChessPiece.PieceType.BISHOP);
                possibleMoves.add(bishopPromotionMove);

                ChessMove knightPromotionMove = new ChessMove(myPosition, downPosition, ChessPiece.PieceType.KNIGHT);
                possibleMoves.add(knightPromotionMove);

                ChessMove rookPromotionMove = new ChessMove(myPosition, downPosition, ChessPiece.PieceType.ROOK);
                possibleMoves.add(rookPromotionMove);

                ChessMove queenPromotionMove = new ChessMove(myPosition, downPosition, ChessPiece.PieceType.QUEEN);
                possibleMoves.add(queenPromotionMove);
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
                ChessMove bishopPromotionMove = new ChessMove(myPosition, upRightPosition, ChessPiece.PieceType.BISHOP);
                possibleMoves.add(bishopPromotionMove);

                ChessMove knightPromotionMove = new ChessMove(myPosition, upRightPosition, ChessPiece.PieceType.KNIGHT);
                possibleMoves.add(knightPromotionMove);

                ChessMove rookPromotionMove = new ChessMove(myPosition, upRightPosition, ChessPiece.PieceType.ROOK);
                possibleMoves.add(rookPromotionMove);

                ChessMove queenPromotionMove = new ChessMove(myPosition, upRightPosition, ChessPiece.PieceType.QUEEN);
                possibleMoves.add(queenPromotionMove);
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
                ChessMove bishopPromotionMove = new ChessMove(myPosition, downRightPosition, ChessPiece.PieceType.BISHOP);
                possibleMoves.add(bishopPromotionMove);

                ChessMove knightPromotionMove = new ChessMove(myPosition, downRightPosition, ChessPiece.PieceType.KNIGHT);
                possibleMoves.add(knightPromotionMove);

                ChessMove rookPromotionMove = new ChessMove(myPosition, downRightPosition, ChessPiece.PieceType.ROOK);
                possibleMoves.add(rookPromotionMove);

                ChessMove queenPromotionMove = new ChessMove(myPosition, downRightPosition, ChessPiece.PieceType.QUEEN);
                possibleMoves.add(queenPromotionMove);
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
                ChessMove bishopPromotionMove = new ChessMove(myPosition, upLeftPosition, ChessPiece.PieceType.BISHOP);
                possibleMoves.add(bishopPromotionMove);

                ChessMove knightPromotionMove = new ChessMove(myPosition, upLeftPosition, ChessPiece.PieceType.KNIGHT);
                possibleMoves.add(knightPromotionMove);

                ChessMove rookPromotionMove = new ChessMove(myPosition, upLeftPosition, ChessPiece.PieceType.ROOK);
                possibleMoves.add(rookPromotionMove);

                ChessMove queenPromotionMove = new ChessMove(myPosition, upLeftPosition, ChessPiece.PieceType.QUEEN);
                possibleMoves.add(queenPromotionMove);
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
                ChessMove bishopPromotionMove = new ChessMove(myPosition, downLeftPosition, ChessPiece.PieceType.BISHOP);
                possibleMoves.add(bishopPromotionMove);

                ChessMove knightPromotionMove = new ChessMove(myPosition, downLeftPosition, ChessPiece.PieceType.KNIGHT);
                possibleMoves.add(knightPromotionMove);

                ChessMove rookPromotionMove = new ChessMove(myPosition, downLeftPosition, ChessPiece.PieceType.ROOK);
                possibleMoves.add(rookPromotionMove);

                ChessMove queenPromotionMove = new ChessMove(myPosition, downLeftPosition, ChessPiece.PieceType.QUEEN);
                possibleMoves.add(queenPromotionMove);
            }
            else {
                possibleMoves.add(downLeftMove);
            }
        }

        return possibleMoves;
    }

    private Collection<ChessMove> addPromotionMove(Collection<ChessMove> possibleMoves, ChessPosition currentPosition, ChessPosition promotionPosition) {
        // promotion types
        Collection<ChessPiece.PieceType> promotionTypes = new List(
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.QUEEN
        );


        ChessMove bishopPromotionMove = new ChessMove(currentPosition, promotionPosition, ChessPiece.PieceType.BISHOP);
        possibleMoves.add(bishopPromotionMove);

        // add knight promotion
        ChessMove knightPromotionMove = new ChessMove(currentPosition, promotionPosition, ChessPiece.PieceType.BISHOP);
        possibleMoves.add(bishopPromotionMove);
    }
}
