package calculator;

import java.util.Collection;
import java.util.HashSet;

public class QueenMoveCalculator extends MoveCalculator {
    public QueenMoveCalculator(chess.ChessBoard b, chess.ChessPosition s) {
        super(b, s);
    }

    public Collection<chess.ChessMove> calculateMoves() {
        Collection<chess.ChessMove> moves = new HashSet<>();
        int row = start.getRow();
        int column = start.getColumn();

        int r = row;
        int c = column;

        for (;;) {
            r++;
            c++;
            chess.ChessPosition p = new chess.ChessPosition(r, c);
            chess.ChessMove m = getMove(p, null);
            if (m != null) {
                if (board.getPiece(p) != null) {
                    moves.add(m);
                    break;
                }
                moves.add(m);
            }
            else {
                break;
            }
        }

        r = row;
        c = column;

        for (;;) {
            r++;
            c--;
            chess.ChessPosition p = new chess.ChessPosition(r, c);
            chess.ChessMove m = getMove(p, null);
            if (m != null) {
                if (board.getPiece(p) != null) {
                    moves.add(m);
                    break;
                }
                moves.add(m);
            }
            else {
                break;
            }
        }

        r = row;
        c = column;

        for (;;) {
            r--;
            c++;
            chess.ChessPosition p = new chess.ChessPosition(r, c);
            chess.ChessMove m = getMove(p, null);
            if (m != null) {
                if (board.getPiece(p) != null) {
                    moves.add(m);
                    break;
                }
                moves.add(m);
            }
            else {
                break;
            }
        }

        r = row;
        c = column;

        for (;;) {
            r--;
            c--;
            chess.ChessPosition p = new chess.ChessPosition(r, c);
            chess.ChessMove m = getMove(p, null);
            if (m != null) {
                if (board.getPiece(p) != null) {
                    moves.add(m);
                    break;
                }
                moves.add(m);
            }
            else {
                break;
            }
        }

        r = row;
        c = column;

        for (;;) {
            r++;
            chess.ChessPosition p = new chess.ChessPosition(r, c);
            chess.ChessMove m = getMove(p, null);
            if (m != null) {
                if (board.getPiece(p) != null) {
                    moves.add(m);
                    break;
                }
                moves.add(m);
            }
            else {
                break;
            }
        }

        r = row;

        for (;;) {
            c--;
            chess.ChessPosition p = new chess.ChessPosition(r, c);
            chess.ChessMove m = getMove(p, null);
            if (m != null) {
                if (board.getPiece(p) != null) {
                    moves.add(m);
                    break;
                }
                moves.add(m);
            }
            else {
                break;
            }
        }

        c = column;

        for (;;) {
            r--;
            chess.ChessPosition p = new chess.ChessPosition(r, c);
            chess.ChessMove m = getMove(p, null);
            if (m != null) {
                if (board.getPiece(p) != null) {
                    moves.add(m);
                    break;
                }
                moves.add(m);
            }
            else {
                break;
            }
        }

        r = row;

        for (;;) {
            c++;
            chess.ChessPosition p = new chess.ChessPosition(r, c);
            chess.ChessMove m = getMove(p, null);
            if (m != null) {
                if (board.getPiece(p) != null) {
                    moves.add(m);
                    break;
                }
                moves.add(m);
            }
            else {
                break;
            }
        }

        return moves;
    }
}
