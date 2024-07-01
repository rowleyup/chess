package calculator;

import java.util.Collection;

public abstract class MoveCalculator {
    protected final chess.ChessBoard board;
    protected final chess.ChessPosition start;
    protected final chess.ChessPiece piece;

    public MoveCalculator(chess.ChessBoard b, chess.ChessPosition s) {
        board = b;
        start = s;
        piece = b.getPiece(s);
    }

    public abstract Collection<chess.ChessMove> calculateMoves();

    protected chess.ChessMove getMove(chess.ChessPosition end, chess.ChessPiece.PieceType newType) {
        chess.ChessMove move = null;
        if (isValidMove(end)) {
            move = new chess.ChessMove(start, end, newType);
        }
        return move;
    }

    protected boolean isValidMove(chess.ChessPosition end) {
        if (end.getRow() > 8 || end.getRow() < 1 || end.getColumn() > 8 || end.getColumn() < 1) {
            return false;
        }
        chess.ChessPiece piece2 = board.getPiece(end);
        if (piece2 == null) {
            return true;
        }
        return piece2.getTeamColor() != piece.getTeamColor();
    }
}
