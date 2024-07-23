package calculator;

import java.util.Collection;
import java.util.HashSet;

public class QueenMoveCalculator extends MoveCalculator {
    public QueenMoveCalculator(chess.ChessBoard b, chess.ChessPosition s) {
        super(b, s);
    }

    public Collection<chess.ChessMove> calculateMoves() {
        Collection<chess.ChessMove> moves = new HashSet<>();
        moves.addAll(new BishopMoveCalculator(board, start).calculateMoves());
        moves.addAll(new RookMoveCalculator(board, start).calculateMoves());

        return moves;
    }
}
