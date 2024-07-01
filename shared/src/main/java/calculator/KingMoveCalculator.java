package calculator;

import java.util.Collection;
import java.util.HashSet;

public class KingMoveCalculator extends MoveCalculator {
    public KingMoveCalculator(chess.ChessBoard b, chess.ChessPosition s) {
        super(b, s);
    }

    public Collection<chess.ChessMove> calculateMoves() {
        Collection<chess.ChessMove> moves = new HashSet<>();
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
}
