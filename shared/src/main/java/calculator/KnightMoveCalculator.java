package calculator;

import java.util.Collection;
import java.util.HashSet;

public class KnightMoveCalculator extends MoveCalculator {
    public KnightMoveCalculator(chess.ChessBoard b, chess.ChessPosition s, chess.ChessPiece p) {
        super(b, s, p);
    }

    public Collection<chess.ChessMove> calculateMoves() {
        Collection<chess.ChessMove> moves = new HashSet<>();
        int row = start.getRow();
        int column = start.getColumn();

        chess.ChessPosition p = new chess.ChessPosition(row+2, column+1);
        chess.ChessMove m = getMove(p, null);
        if (m != null) {
            moves.add(m);
        }

        chess.ChessPosition p2 = new chess.ChessPosition(row+2, column-1);
        chess.ChessMove m2 = getMove(p2, null);
        if (m2 != null) {
            moves.add(m2);
        }

        chess.ChessPosition p3 = new chess.ChessPosition(row-2, column+1);
        chess.ChessMove m3 = getMove(p3, null);
        if (m3 != null) {
            moves.add(m3);
        }

        chess.ChessPosition p4 = new chess.ChessPosition(row-2, column-1);
        chess.ChessMove m4 = getMove(p4, null);
        if (m4 != null) {
            moves.add(m4);
        }

        chess.ChessPosition p5 = new chess.ChessPosition(row+1, column+2);
        chess.ChessMove m5 = getMove(p5, null);
        if (m5 != null) {
            moves.add(m5);
        }

        chess.ChessPosition p6 = new chess.ChessPosition(row-1, column+2);
        chess.ChessMove m6 = getMove(p6, null);
        if (m6 != null) {
            moves.add(m6);
        }

        chess.ChessPosition p7 = new chess.ChessPosition(row+1, column-2);
        chess.ChessMove m7 = getMove(p7, null);
        if (m7 != null) {
            moves.add(m7);
        }

        chess.ChessPosition p8 = new chess.ChessPosition(row-1, column-2);
        chess.ChessMove m8 = getMove(p8, null);
        if (m8 != null) {
            moves.add(m8);
        }

        return moves;
    }
}
