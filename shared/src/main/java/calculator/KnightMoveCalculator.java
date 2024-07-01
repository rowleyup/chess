package calculator;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMoveCalculator implements MoveCalculator {
    private final chess.ChessBoard board;
    private final chess.ChessPosition start;
    private final chess.ChessPiece piece;

    public KnightMoveCalculator(chess.ChessBoard b, chess.ChessPosition s, chess.ChessPiece p) {
        board = b;
        start = s;
        piece = p;
    }

    public Collection<chess.ChessMove> calculateMoves() {
        Collection<chess.ChessMove> moves = new ArrayList<chess.ChessMove>();
        int row = start.getRow();
        int column = start.getColumn();

        if (row+2 <= 8) {
            if (column+1 <= 8) {
                chess.ChessPosition p = new chess.ChessPosition(row+2, column+1);
                chess.ChessMove m = getMove(p, null);
                if (m != null) {
                    moves.add(m);
                }
            }
            if (column-1 > 0) {
                chess.ChessPosition p2 = new chess.ChessPosition(row+2, column-1);
                chess.ChessMove m2 = getMove(p2, null);
                if (m2 != null) {
                    moves.add(m2);
                }
            }
        }

        if (row-2 > 0) {
            if (column+1 <= 8) {
                chess.ChessPosition p3 = new chess.ChessPosition(row-2, column+1);
                chess.ChessMove m3 = getMove(p3, null);
                if (m3 != null) {
                    moves.add(m3);
                }
            }
            if (column-1 > 0) {
                chess.ChessPosition p4 = new chess.ChessPosition(row-2, column-1);
                chess.ChessMove m4 = getMove(p4, null);
                if (m4 != null) {
                    moves.add(m4);
                }
            }
        }

        if (column+2 <= 8) {
            if (row+1 <= 8) {
                chess.ChessPosition p5 = new chess.ChessPosition(row+1, column+2);
                chess.ChessMove m5 = getMove(p5, null);
                if (m5 != null) {
                    moves.add(m5);
                }
            }
            if (row-1 > 0) {
                chess.ChessPosition p6 = new chess.ChessPosition(row-1, column+2);
                chess.ChessMove m6 = getMove(p6, null);
                if (m6 != null) {
                    moves.add(m6);
                }
            }
        }

        if (column-2 > 0) {
            if (row+1 <= 8) {
                chess.ChessPosition p7 = new chess.ChessPosition(row+1, column-2);
                chess.ChessMove m7 = getMove(p7, null);
                if (m7 != null) {
                    moves.add(m7);
                }
            }
            if (row-1 > 0) {
                chess.ChessPosition p8 = new chess.ChessPosition(row-1, column-2);
                chess.ChessMove m8 = getMove(p8, null);
                if (m8 != null) {
                    moves.add(m8);
                }
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
