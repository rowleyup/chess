package calculator;

import java.util.Collection;
import java.util.HashSet;

public class BishopRookMoves extends MoveCalculator{
    private final Collection<chess.ChessMove> moves;

    public BishopRookMoves(chess.ChessBoard b, chess.ChessPosition s){
        super(b,s);
        moves = new HashSet<>();
    }

    public Collection<chess.ChessMove> calculateMoves() {
        return moves;
    }

    public boolean checkMove(chess.ChessPosition p) {
        chess.ChessMove m = getMove(p, null);
        if (m != null) {
            if (board.getPiece(p) != null) {
                moves.add(m);
                return false;
            }
            moves.add(m);
        }
        else {
            return false;
        }
        return true;
    }
}
