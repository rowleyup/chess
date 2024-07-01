package calculator;

import java.util.Collection;

interface MoveCalculator {
    public abstract Collection<chess.ChessMove> calculateMoves();
    public abstract chess.ChessMove getMove(chess.ChessPosition end, chess.ChessPiece.PieceType newType);
    public abstract boolean isValidMove(chess.ChessPosition end);
}
