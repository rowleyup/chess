package calculator;

import java.util.ArrayList;
import java.util.Collection;

public class RookMoveCalculator implements MoveCalculator {
    private final chess.ChessBoard board;
    private final chess.ChessPosition start;
    private final chess.ChessPiece piece;

    public RookMoveCalculator(chess.ChessBoard b, chess.ChessPosition s, chess.ChessPiece p) {
        board = b;
        start = s;
        piece = p;
    }

    public Collection<chess.ChessMove> calculateMoves() {
        Collection<chess.ChessMove> moves = new ArrayList<chess.ChessMove>();
        int row = start.getRow();
        int column = start.getColumn();

        int r = row;
        int c = column;

        while (r < 8) {
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

        while (c > 1) {
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

        while (r > 1) {
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

        while (c < 8) {
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

    public chess.ChessMove getMove(chess.ChessPosition end, chess.ChessPiece.PieceType newType) {
        chess.ChessMove move = null;
        if (isValidMove(end)) {
            move = new chess.ChessMove(start, end, newType);
        }
        return move;
    }

    public boolean isValidMove(chess.ChessPosition end) {
        chess.ChessPiece piece2 = board.getPiece(end);
        if (piece2 == null) {
            return true;
        }
        return piece2.getTeamColor() != piece.getTeamColor();
    }
}
