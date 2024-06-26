package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    public ChessPiece[][] board;

    public ChessBoard() {
        board = new ChessPiece[8][8];
        clearBoard();
        resetHelper();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        int row = position.getRow() - 1;
        int column = position.getColumn() - 1;
        board[row][column] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        int row = position.getRow() - 1;
        int column = position.getColumn() - 1;
        return board[row][column];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        clearBoard();
        resetHelper();
    }

    /**
     * Places the pieces on the board in normal starting position
     */
    private void resetHelper() {
        for (ChessPiece spot : board[1]) {
            spot = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        }
        for (ChessPiece spot : board[6]) {
            spot = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }

        ChessPiece.PieceType[] order = {ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KING, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK};
        for (int i = 0; i < 8; i++) {
            board[0][i] = new ChessPiece(ChessGame.TeamColor.WHITE, order[i]);
            board[7][i] = new ChessPiece(ChessGame.TeamColor.BLACK, order[i]);
        }
    }

    /**
     * Removes all pieces from the board
     */
    private void clearBoard() {
        for (ChessPiece[] row : board) {
            for (ChessPiece square : row) {
                square = null;
            }
        }
    }
}
