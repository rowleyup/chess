package calculator;

import java.util.ArrayList;
import java.util.Collection;

public class KingMoveCalculator implements MoveCalculator {
    public Collection<chess.ChessMove> calculateMoves(chess.ChessBoard board, chess.ChessPosition start, chess.ChessPiece piece) {
        Collection<chess.ChessMove> moves = new ArrayList<chess.ChessMove>();
        int row = start.getRow() - 1;
        int column = start.getColumn() - 1;

        if (row + 1 <= 7) {
            chess.ChessPosition position = new chess.ChessPosition(row+1, column);
            chess.ChessMove m = getMove(board, position, start, piece);
            if (m != null) {
                moves.add(m);
            }
            if (column + 1 <= 7) {
                chess.ChessPosition position2 = new chess.ChessPosition(row+1, column+1);
                chess.ChessMove m2 = getMove(board, position2, start, piece);
                if (m2 != null) {
                    moves.add(m2);
                }
                chess.ChessPosition position4 = new chess.ChessPosition(row, column-1);
                chess.ChessMove m4 = getMove(board, position4, start, piece);
                if (m4 != null) {
                    moves.add(m4);
                }
            }
            if (column - 1 >= 0) {
                chess.ChessPosition position3 = new chess.ChessPosition(row+1, column-1);
                chess.ChessMove m3 = getMove(board, position3, start, piece);
                if (m3 != null) {
                    moves.add(m3);
                }
                chess.ChessPosition position5 = new chess.ChessPosition(row, column-1);
                chess.ChessMove m5 = getMove(board, position5, start, piece);
                if (m5 != null) {
                    moves.add(m5);
                }
            }
        }

        if (row - 1 >= 0) {
            chess.ChessPosition position = new chess.ChessPosition(row-1, column);
            chess.ChessMove m = getMove(board, position, start, piece);
            if (m != null) {
                moves.add(m);
            }
            if (column + 1 <= 7) {
                chess.ChessPosition position2 = new chess.ChessPosition(row-1, column+1);
                chess.ChessMove m2 = getMove(board, position2, start, piece);
                if (m2 != null) {
                    moves.add(m2);
                }
            }
            if (column - 1 >= 0) {
                chess.ChessPosition position3 = new chess.ChessPosition(row-1, column-1);
                chess.ChessMove m3 = getMove(board, position3, start, piece);
                if (m3 != null) {
                    moves.add(m3);
                }
            }
        }

        return moves;
    }

    public chess.ChessMove getMove(chess.ChessBoard board, chess.ChessPosition start, chess.ChessPosition end, chess.ChessPiece piece) {
        chess.ChessMove move = null;
        if (isValidMove(board, end, piece.getTeamColor())) {
            if (isPromote(end, piece.getTeamColor())) {
                move = new chess.ChessMove(start, end, chess.ChessPiece.PieceType.QUEEN);
            }
            else {
                move = new chess.ChessMove(start, end, piece.getPieceType());
            }
        }
        return move;
    }

    public boolean isValidMove(chess.ChessBoard board, chess.ChessPosition end, chess.ChessGame.TeamColor team) {
        chess.ChessPiece piece = board.getPiece(end);
        if (piece == null) {
            return true;
        }
        return piece.getTeamColor() != team;
    }

    public boolean isPromote(chess.ChessPosition end, chess.ChessGame.TeamColor team) {
        if (team == chess.ChessGame.TeamColor.BLACK && end.getRow() == 0) {
            return true;
        }
        return team == chess.ChessGame.TeamColor.WHITE && end.getRow() == 7;
    }
}
