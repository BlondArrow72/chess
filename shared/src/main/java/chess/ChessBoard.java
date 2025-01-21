package chess;

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
        resetBoard();
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
    public ChessPiece getPiece(ChessPosition position) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        // helper arrays
        char[] startingBaseRow = {'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'};

        char[] startingEndRow[8];
        for (int i = 0; i < 8; i ++) {
            startingEndRow[i] = startingBaseRow[-1 - i];
        }

        // set base row and end row
        chessBoard[0] = startingBaseRow;
        chessBoard[-1] = startingEndRow;

        //

    }
}
