package calculator;

import java.util.Collection;
import java.util.HashSet;

public class PawnMoveCalculator implements MoveCalculator {
    private final chess.ChessBoard board;
    private final chess.ChessPosition start;
    private final chess.ChessPiece piece;

    public PawnMoveCalculator(chess.ChessBoard b, chess.ChessPosition s, chess.ChessPiece p) {
        board = b;
        start = s;
        piece = p;
    }

    public Collection<chess.ChessMove> calculateMoves() {
        HashSet<chess.ChessMove> moves = new HashSet<>();
        if (piece.getTeamColor() == chess.ChessGame.TeamColor.BLACK) {
            if (isPromote()) {
                moves.addAll(blackMove(chess.ChessPiece.PieceType.QUEEN));
                moves.addAll(blackMove(chess.ChessPiece.PieceType.BISHOP));
                moves.addAll(blackMove(chess.ChessPiece.PieceType.ROOK));
                moves.addAll(blackMove(chess.ChessPiece.PieceType.KNIGHT));
            }
            else {
                moves.addAll(blackMove(null));
            }
        }
        else {
            if (isPromote()) {
                moves.addAll(whiteMove(chess.ChessPiece.PieceType.QUEEN));
                moves.addAll(whiteMove(chess.ChessPiece.PieceType.BISHOP));
                moves.addAll(whiteMove(chess.ChessPiece.PieceType.ROOK));
                moves.addAll(whiteMove(chess.ChessPiece.PieceType.KNIGHT));
            }
            else {
                moves.addAll(whiteMove(null));
            }
        }

        return moves;
    }

    public chess.ChessMove getMove(chess.ChessPosition end, chess.ChessPiece.PieceType newType) {
        chess.ChessMove move = null;
        int row = end.getRow();
        int column = end.getColumn();
        if (row > 8 || row < 1 || column > 8 || column < 1) {
            return move;
        }
        if (isValidMove(end)) {
            move = new chess.ChessMove(start, end, newType);
        }
        return move;
    }

    public boolean isValidMove(chess.ChessPosition end) {
        int col_diff = end.getColumn() - start.getColumn();
        chess.ChessPiece piece2 = board.getPiece(end);
        if (col_diff != 0) {
            if (piece2 == null) {
                return false;
            }
            return piece2.getTeamColor() != piece.getTeamColor();
        }
        return piece2 == null;
    }

    public boolean isPromote() {
        if (piece.getTeamColor() == chess.ChessGame.TeamColor.BLACK && start.getRow() == 2) {
            return true;
        }
        return piece.getTeamColor() == chess.ChessGame.TeamColor.WHITE && start.getRow() == 7;
    }

    private Collection<chess.ChessMove> whiteMove (chess.ChessPiece.PieceType type) {
        Collection<chess.ChessMove> moves = new HashSet<>();
        int row = start.getRow();
        int column = start.getColumn();
        chess.ChessPosition position = new chess.ChessPosition(row + 1, column);
        chess.ChessMove m = getMove(position, type);
        if (m != null) {
            moves.add(m);
        }
        chess.ChessPosition position2 = new chess.ChessPosition(row + 1, column + 1);
        chess.ChessMove m2 = getMove(position2, type);
        if (m2 != null) {
            moves.add(m2);
        }
        chess.ChessPosition position3 = new chess.ChessPosition(row + 1, column - 1);
        chess.ChessMove m3 = getMove(position3, type);
        if (m3 != null) {
            moves.add(m3);
        }

        if (row == 2 && board.getPiece(new chess.ChessPosition(row + 1, column)) == null) {
            chess.ChessPosition position4 = new chess.ChessPosition(row + 2, column);
            chess.ChessMove m4 = getMove(position4, type);
            if (m4 != null) {
                moves.add(m4);
            }
        }
        return moves;
    }

    private Collection<chess.ChessMove> blackMove (chess.ChessPiece.PieceType type) {
        Collection<chess.ChessMove> moves = new HashSet<chess.ChessMove>();
        int row = start.getRow();
        int column = start.getColumn();
        chess.ChessPosition position = new chess.ChessPosition(row - 1, column);
        chess.ChessMove m = getMove(position, type);
        if (m != null) {
            moves.add(m);
        }
        chess.ChessPosition position2 = new chess.ChessPosition(row - 1, column + 1);
        chess.ChessMove m2 = getMove(position2, type);
        if (m2 != null) {
            moves.add(m2);
        }
        chess.ChessPosition position3 = new chess.ChessPosition(row - 1, column - 1);
        chess.ChessMove m3 = getMove(position3, type);
        if (m3 != null) {
            moves.add(m3);
        }

        if (row == 7 && board.getPiece(new chess.ChessPosition(row - 1, column)) == null) {
            chess.ChessPosition position4 = new chess.ChessPosition(row - 2, column);
            chess.ChessMove m4 = getMove(position4, type);
            if (m4 != null) {
                moves.add(m4);
            }
        }
        return moves;
    }
}
