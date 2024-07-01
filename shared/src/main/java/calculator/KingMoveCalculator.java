package calculator;

import java.util.ArrayList;
import java.util.Collection;

public class KingMoveCalculator implements MoveCalculator {
    private final chess.ChessBoard board;
    private final chess.ChessPosition start;
    private final chess.ChessPiece piece;

    public KingMoveCalculator(chess.ChessBoard b, chess.ChessPosition s, chess.ChessPiece p) {
        board = b;
        start = s;
        piece = p;
    }

    public Collection<chess.ChessMove> calculateMoves() {
        Collection<chess.ChessMove> moves = new ArrayList<chess.ChessMove>();
        int row = start.getRow();
        int column = start.getColumn();

        chess.ChessPosition position = new chess.ChessPosition(row+1, column);
        chess.ChessMove m = getMove(position, null);
        if (m != null) {
            moves.add(m);
        }

        position = new chess.ChessPosition(row+1, column+1);
        m = getMove(position, null);
        if (m != null) {
            moves.add(m);
        }

        position = new chess.ChessPosition(row, column+1);
        m = getMove(position, null);
        if (m != null) {
            moves.add(m);
        }

        position = new chess.ChessPosition(row-1, column+1);
        m = getMove(position, null);
        if (m != null) {
            moves.add(m);
        }

        position = new chess.ChessPosition(row-1, column);
        m = getMove(position, null);
        if (m != null) {
            moves.add(m);
        }

        position = new chess.ChessPosition(row-1, column-1);
        m = getMove(position, null);
        if (m != null) {
            moves.add(m);
        }

        position = new chess.ChessPosition(row, column-1);
        m = getMove(position, null);
        if (m != null) {
            moves.add(m);
        }

        position = new chess.ChessPosition(row+1, column-1);
        m = getMove(position, null);
        if (m != null) {
            moves.add(m);
        }

        return moves;
    }

    public chess.ChessMove getMove(chess.ChessPosition end, chess.ChessPiece.PieceType newType) {
        chess.ChessMove move = null;
        if (isValidMove(end)) {
            move = new chess.ChessMove(start, end, newType);
        }
        return move;
    }

    public boolean isValidMove(chess.ChessPosition end) {
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
