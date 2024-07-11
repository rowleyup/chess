package calculator;

import java.util.Collection;
import java.util.HashSet;

public class PawnMoveCalculator extends MoveCalculator {
    public PawnMoveCalculator(chess.ChessBoard b, chess.ChessPosition s) {
        super(b, s);
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

    @Override
    public boolean isValidMove(chess.ChessPosition end) {
        if (end.getRow() > 8 || end.getRow() < 1 || end.getColumn() > 8 || end.getColumn() < 1) {
            return false;
        }
        int col_diff = end.getColumn() - start.getColumn();
        chess.ChessPiece piece2 = board.getPiece(end);
        if (col_diff != 0) {
            if (piece2 == null) {
                if (piece.getEnPassant()) {
                    chess.ChessPosition pos = new chess.ChessPosition(start.getRow(), start.getColumn()+col_diff);
                    chess.ChessPiece piece3 = board.getPiece(pos);
                    if (piece3 != null) {
                        return piece3.getTeamColor() != piece.getTeamColor() && piece3.getPieceType() == chess.ChessPiece.PieceType.PAWN;
                    }
                    return false;
                }
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
        chess.ChessPosition p = new chess.ChessPosition(row + 1, column);
        chess.ChessMove m = getMove(p, type);
        if (m != null) {
            moves.add(m);
        }
        p = new chess.ChessPosition(row + 1, column + 1);
        m = getMove(p, type);
        if (m != null) {
            moves.add(m);
        }
        p = new chess.ChessPosition(row + 1, column - 1);
        m = getMove(p, type);
        if (m != null) {
            moves.add(m);
        }

        if (row == 2 && board.getPiece(new chess.ChessPosition(row + 1, column)) == null) {
            p = new chess.ChessPosition(row + 2, column);
            m = getMove(p, type);
            if (m != null) {
                moves.add(m);
            }
        }

        if (board.getPiece(start).getEnPassant()) {
            p = new chess.ChessPosition(start.getRow()+1, start.getColumn()-1);
            m = getMove(p, null);
            if (m != null) {
                moves.add(m);
            }

            p = new chess.ChessPosition(start.getRow()+1, start.getColumn()+1);
            m = getMove(p, null);
            if (m != null) {
                moves.add(m);
            }
        }

        return moves;
    }

    private Collection<chess.ChessMove> blackMove (chess.ChessPiece.PieceType type) {
        Collection<chess.ChessMove> moves = new HashSet<>();
        int row = start.getRow();
        int column = start.getColumn();
        chess.ChessPosition p = new chess.ChessPosition(row - 1, column);
        chess.ChessMove m = getMove(p, type);
        if (m != null) {
            moves.add(m);
        }
        p = new chess.ChessPosition(row - 1, column + 1);
        m = getMove(p, type);
        if (m != null) {
            moves.add(m);
        }
        p = new chess.ChessPosition(row - 1, column - 1);
        m = getMove(p, type);
        if (m != null) {
            moves.add(m);
        }

        if (row == 7 && board.getPiece(new chess.ChessPosition(row - 1, column)) == null) {
            p = new chess.ChessPosition(row - 2, column);
            m = getMove(p, type);
            if (m != null) {
                moves.add(m);
            }
        }

        if (board.getPiece(start).getEnPassant()) {
            p = new chess.ChessPosition(start.getRow()-1, start.getColumn()-1);
            m = getMove(p, null);
            if (m != null) {
                moves.add(m);
            }

            p = new chess.ChessPosition(start.getRow()-1, start.getColumn()+1);
            m = getMove(p, null);
            if (m != null) {
                moves.add(m);
            }
        }

        return moves;
    }
}
