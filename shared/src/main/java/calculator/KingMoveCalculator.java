package calculator;

import java.util.ArrayList;
import java.util.Collection;

public class KingMoveCalculator implements MoveCalculator {
    private final chess.ChessBoard board;
    private final chess.ChessPosition start;
    private final chess.ChessPiece piece;

    public KingMoveCalculator(chess.ChessBoard b, chess.ChessPosition s, chess.ChessPiece p) {
        board = b;
        start = s;
        piece = p;
    }

    public Collection<chess.ChessMove> calculateMoves() {
        Collection<chess.ChessMove> moves = new ArrayList<chess.ChessMove>();
        int row = start.getRow();
        int column = start.getColumn();

        chess.ChessPosition position = new chess.ChessPosition(row+1, column);
        chess.ChessMove m = getMove(board, start, position, piece);
        if (m != null) {
            moves.add(m);
        }

        position = new chess.ChessPosition(row+1, column+1);
        m = getMove(board, start, position, piece);
        if (m != null) {
            moves.add(m);
        }

        position = new chess.ChessPosition(row, column+1);
        m = getMove(board, start, position, piece);
        if (m != null) {
            moves.add(m);
        }

        position = new chess.ChessPosition(row-1, column+1);
        m = getMove(board, start, position, piece);
        if (m != null) {
            moves.add(m);
        }

        position = new chess.ChessPosition(row-1, column);
        m = getMove(board, start, position, piece);
        if (m != null) {
            moves.add(m);
        }

        position = new chess.ChessPosition(row-1, column-1);
        m = getMove(board, start, position, piece);
        if (m != null) {
            moves.add(m);
        }

        position = new chess.ChessPosition(row, column-1);
        m = getMove(board, start, position, piece);
        if (m != null) {
            moves.add(m);
        }

        position = new chess.ChessPosition(row+1, column-1);
        m = getMove(board, start, position, piece);
        if (m != null) {
            moves.add(m);
        }

        return moves;
    }

    public chess.ChessMove getMove(chess.ChessBoard board, chess.ChessPosition start, chess.ChessPosition end, chess.ChessPiece piece) {
        chess.ChessMove move = null;
        if (isValidMove(board, start, end, piece.getTeamColor())) {
            move = new chess.ChessMove(start, end, null);
        }
        return move;
    }

    public boolean isValidMove(chess.ChessBoard board, chess.ChessPosition start, chess.ChessPosition end, chess.ChessGame.TeamColor team) {
        if (end.getRow() > 8 || end.getRow() < 1 || end.getColumn() > 8 || end.getColumn() < 1) {
            return false;
        }
        chess.ChessPiece piece = board.getPiece(end);
        if (piece == null) {
            return true;
        }
        return piece.getTeamColor() != team;
    }
}
