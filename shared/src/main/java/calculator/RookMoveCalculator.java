package calculator;

import java.util.Collection;

public class RookMoveCalculator extends MoveCalculator {
    public RookMoveCalculator(chess.ChessBoard b, chess.ChessPosition s) {
        super(b, s);
    }

    public Collection<chess.ChessMove> calculateMoves() {
        int row = start.getRow();
        int column = start.getColumn();
        BishopRookMoves brk = new BishopRookMoves(board, start);

        int r = row;
        int c = column;
        boolean going = true;

        while (going) {
            r++;
            chess.ChessPosition p = new chess.ChessPosition(r, c);
            going = brk.checkMove(p);
        }

        r = row;
        going = true;

        while (going) {
            c--;
            chess.ChessPosition p = new chess.ChessPosition(r, c);
            going = brk.checkMove(p);
        }

        c = column;
        going = true;

        while (going) {
            r--;
            chess.ChessPosition p = new chess.ChessPosition(r, c);
            going = brk.checkMove(p);
        }

        r = row;
        going = true;

        while (going) {
            c++;
            chess.ChessPosition p = new chess.ChessPosition(r, c);
            going = brk.checkMove(p);
        }

        return brk.calculateMoves();
    }
}
