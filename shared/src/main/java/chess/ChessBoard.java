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
        if (row >= 8 || row < 0 || column >= 8 || column < 0) {
            throw new IndexOutOfBoundsException();
        }
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
        ChessPiece.PieceType[] order = {ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KING,
                ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK};

        for (int i = 1; i <= 8; i++) {
            ChessPiece p = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            ChessPosition pos = new ChessPosition(2, i);
            addPiece(pos, p);

            p = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            pos = new ChessPosition(7, i);
            addPiece(pos, p);

            p = new ChessPiece(ChessGame.TeamColor.WHITE, order[i]);
            pos = new ChessPosition(1, i+1);
            addPiece(pos, p);

            p = new ChessPiece(ChessGame.TeamColor.BLACK, order[i]);
            pos = new ChessPosition(8, i+1);
            addPiece(pos, p);
        }
    }

    /**
     * Removes all pieces from the board
     */
    private void clearBoard() {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition p = new ChessPosition(i, j);
                addPiece(p, null);
            }
        }
    }

    @Override
    public String toString() {
        return String.format("[%s]", getClass().getName());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        ChessBoard other = (ChessBoard)obj;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition p = new ChessPosition(i, j);
                ChessPiece pie = getPiece(p);
                ChessPiece qie = other.getPiece(p);
                if (pie == null) {
                    return qie == null;
                }
                if (!pie.equals(qie)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition p = new ChessPosition(i, j);
                ChessPiece pie = getPiece(p);
                if (pie != null) {
                    result = 31 * result + pie.hashCode();
                }
                else {
                    result++;
                }
            }
        }
        return result;
    }
}
