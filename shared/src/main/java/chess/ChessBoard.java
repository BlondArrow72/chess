package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    // Use 2d array to store board
    private ChessPiece[][] chessBoard;
    private final int nRowCol = 8;

    public ChessBoard() {
        this.chessBoard = new ChessPiece[nRowCol][nRowCol];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        this.chessBoard[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return this.chessBoard[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        // clear board
        this.chessBoard = new ChessPiece[nRowCol][nRowCol];

        // helper array
        ChessPiece.PieceType [] helperArray = {
                ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.KING,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.ROOK
        };

        // fill in board
        for (int i = 0; i < 8; i++) {
            // base row
            this.chessBoard[0][i] = new ChessPiece(ChessGame.TeamColor.WHITE, helperArray[i]);

            // pawn rows
            this.chessBoard[1][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            this.chessBoard[6][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);

            // end row
            this.chessBoard[7][7-i] = new ChessPiece(ChessGame.TeamColor.BLACK, helperArray[7-i]);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(chessBoard, that.chessBoard);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(chessBoard);
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "chessBoard=" + Arrays.toString(chessBoard) +
                '}';
    }
}
