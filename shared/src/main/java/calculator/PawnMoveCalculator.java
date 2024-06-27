package calculator;

import chess.ChessMove;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoveCalculator implements MoveCalculator {
    public Collection<ChessMove> calculateMoves(chess.ChessBoard board, chess.ChessPosition start, chess.ChessPiece piece) {
        Collection<chess.ChessMove> moves = new ArrayList<ChessMove>();
        int row = start.getRow() - 1;
        int column = start.getColumn() - 1;

        if (piece.getTeamColor() == chess.ChessGame.TeamColor.BLACK) {
            chess.ChessPosition position = new chess.ChessPosition(row-1, column);
            chess.ChessMove m = getMove(board,  start, position, piece);
            if (m != null) {
                moves.add(m);
            }
            chess.ChessPosition position2 = new chess.ChessPosition(row-1, column+1);
            chess.ChessMove m2 = getMove(board, start, position2, piece);
            if (m2 != null) {
                moves.add(m2);
            }
            chess.ChessPosition position3 = new chess.ChessPosition(row-1, column-1);
            chess.ChessMove m3 = getMove(board, start, position3, piece);
            if (m3 != null) {
                moves.add(m3);
            }

            if (row == 6) {
                chess.ChessPosition position4 = new chess.ChessPosition(row-2, column);
                chess.ChessMove m4 = getMove(board, start, position4, piece);
                if (m4 != null) {
                    moves.add(m4);
                }
            }
        }
        else if (piece.getTeamColor() == chess.ChessGame.TeamColor.WHITE) {
            chess.ChessPosition position = new chess.ChessPosition(row+1, column);
            chess.ChessMove m = getMove(board,  start, position, piece);
            if (m != null) {
                moves.add(m);
            }
            chess.ChessPosition position2 = new chess.ChessPosition(row+1, column+1);
            chess.ChessMove m2 = getMove(board, start, position2, piece);
            if (m2 != null) {
                moves.add(m2);
            }
            chess.ChessPosition position3 = new chess.ChessPosition(row+1, column-1);
            chess.ChessMove m3 = getMove(board, start, position3, piece);
            if (m3 != null) {
                moves.add(m3);
            }

            if (row == 1) {
                chess.ChessPosition position4 = new chess.ChessPosition(row+2, column);
                chess.ChessMove m4 = getMove(board, start, position4, piece);
                if (m4 != null) {
                    moves.add(m4);
                }
            }
        }

        return moves;
    }

    public chess.ChessMove getMove(chess.ChessBoard board, chess.ChessPosition start, chess.ChessPosition end, chess.ChessPiece piece) {
        chess.ChessMove move = null;
        if (isValidMove(board, start, end, piece.getTeamColor())) {
            if (isPromote(end, piece.getTeamColor())) {
                move = new chess.ChessMove(start, end, chess.ChessPiece.PieceType.QUEEN);
            }
            else {
                move = new chess.ChessMove(start, end, piece.getPieceType());
            }
        }
        return move;
    }

    public boolean isValidMove(chess.ChessBoard board, chess.ChessPosition start, chess.ChessPosition end, chess.ChessGame.TeamColor team) {
        int col_diff = end.getColumn() - start.getColumn();
        chess.ChessPiece piece = board.getPiece(end);
        if (col_diff != 0) {
            if (piece == null) {
                return false;
            }
            return piece.getTeamColor() != team;
        }
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
