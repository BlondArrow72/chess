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
    private char[][] chessBoard = new char[8][8];

    public ChessBoard() {
        this.resetBoard();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {

        throw new RuntimeException("Not implemented");
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) { throw new RuntimeException("Not implemented");
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        // helper array
        char[] startingBaseRow = {'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'};

        // fill in board
        for (int i = 0; i < 8; i ++) {
            // base row
            this.chessBoard[0][i] = startingBaseRow[i];

            // pawn row
            this.chessBoard[1][i] = 'P';

            // 4 empty spaces
            this.chessBoard[2][i] = ' ';
            this.chessBoard[3][i] = ' ';
            this.chessBoard[4][i] = ' ';
            this.chessBoard[5][i] = ' ';

            // pawn row
            this.chessBoard[6][i] = 'P';

            // end row, which is startingBaseRow but backwards
            this.chessBoard[7][i] = startingBaseRow[7 - i];
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
