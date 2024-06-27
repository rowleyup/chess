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
        int row = start.getRow() - 1;
        int column = start.getColumn() - 1;

        if (row+2 < 8) {
            if (column+1 < 8) {
                chess.ChessPosition p = new chess.ChessPosition(row+2, column+1);
                chess.ChessMove m = getMove(board, start, p, piece);
                if (m != null) {
                    moves.add(m);
                }
            }
            if (column-1 >= 0) {
                chess.ChessPosition p2 = new chess.ChessPosition(row+2, column-1);
                chess.ChessMove m2 = getMove(board, start, p2, piece);
                if (m2 != null) {
                    moves.add(m2);
                }
            }
        }

        if (row-2 >= 0) {
            if (column+1 < 8) {
                chess.ChessPosition p3 = new chess.ChessPosition(row-2, column+1);
                chess.ChessMove m3 = getMove(board, start, p3, piece);
                if (m3 != null) {
                    moves.add(m3);
                }
            }
            if (column-1 >= 0) {
                chess.ChessPosition p4 = new chess.ChessPosition(row-2, column-1);
                chess.ChessMove m4 = getMove(board, start, p4, piece);
                if (m4 != null) {
                    moves.add(m4);
                }
            }
        }

        if (column+2 < 8) {
            if (row+1 < 8) {
                chess.ChessPosition p5 = new chess.ChessPosition(row+1, column+2);
                chess.ChessMove m5 = getMove(board, start, p5, piece);
                if (m5 != null) {
                    moves.add(m5);
                }
            }
            if (row-1 >= 0) {
                chess.ChessPosition p6 = new chess.ChessPosition(row-1, column+2);
                chess.ChessMove m6 = getMove(board, start, p6, piece);
                if (m6 != null) {
                    moves.add(m6);
                }
            }
        }

        if (column-2 < 8) {
            if (row+1 < 8) {
                chess.ChessPosition p7 = new chess.ChessPosition(row+1, column-2);
                chess.ChessMove m7 = getMove(board, start, p7, piece);
                if (m7 != null) {
                    moves.add(m7);
                }
            }
            if (row-1 >= 0) {
                chess.ChessPosition p8 = new chess.ChessPosition(row-1, column-2);
                chess.ChessMove m8 = getMove(board, start, p8, piece);
                if (m8 != null) {
                    moves.add(m8);
                }
            }
        }

        return moves;
    }

    public chess.ChessMove getMove(chess.ChessBoard board, chess.ChessPosition start, chess.ChessPosition end, chess.ChessPiece piece) {
        chess.ChessMove move = null;
        if (isValidMove(board, start, end, piece.getTeamColor())) {
            move = new chess.ChessMove(start, end, piece.getPieceType());
        }
        return move;
    }

    public boolean isValidMove(chess.ChessBoard board, chess.ChessPosition start, chess.ChessPosition end, chess.ChessGame.TeamColor team) {
        chess.ChessPiece piece = board.getPiece(end);
        if (piece == null) {
            return true;
        }
        return piece.getTeamColor() != team;
    }
}
