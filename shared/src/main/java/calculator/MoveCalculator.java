package calculator;

import java.util.Collection;

interface MoveCalculator {
    public abstract Collection<chess.ChessMove> calculateMoves();
    public abstract chess.ChessMove getMove(chess.ChessBoard board, chess.ChessPosition start, chess.ChessPosition end, chess.ChessPiece piece);
    public abstract boolean isValidMove(chess.ChessBoard board, chess.ChessPosition start, chess.ChessPosition end, chess.ChessGame.TeamColor team);
}
