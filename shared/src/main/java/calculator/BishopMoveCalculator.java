package calculator;

import java.util.Collection;

public class BishopMoveCalculator extends MoveCalculator {
    public BishopMoveCalculator(chess.ChessBoard b, chess.ChessPosition s) {
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
            c++;
            chess.ChessPosition p = new chess.ChessPosition(r, c);
            going = brk.checkMove(p);
        }

        r = row;
        c = column;
        going = true;

        while (going) {
            r--;
            c++;
            chess.ChessPosition p = new chess.ChessPosition(r, c);
            going = brk.checkMove(p);
        }

        r = row;
        c = column;
        going = true;

        while (going) {
            r--;
            c--;
            chess.ChessPosition p = new chess.ChessPosition(r, c);
            going = brk.checkMove(p);
        }

        r = row;
        c = column;
        going = true;

        while (going) {
            r++;
            c--;
            chess.ChessPosition p = new chess.ChessPosition(r, c);
            going = brk.checkMove(p);
        }

        return brk.calculateMoves();
    }
}
