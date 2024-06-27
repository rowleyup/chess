package calculator;

import java.util.Collection;

interface MoveCalculator {
    public abstract Collection<chess.ChessMove> calculateMoves(chess.ChessBoard board, chess.ChessPosition start, chess.ChessPiece piece);
    public abstract chess.ChessMove getMove(chess.ChessBoard board, chess.ChessPosition start, chess.ChessPosition end, chess.ChessPiece piece);
    public abstract boolean isValidMove(chess.ChessBoard board, chess.ChessPosition end, chess.ChessGame.TeamColor team);
    public abstract boolean isPromote(chess.ChessPosition end, chess.ChessGame.TeamColor team);
}
