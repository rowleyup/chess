package chess;

import calculator.*;

import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor color;
    private final ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType pieceType) {
        color = pieceColor;
        type = pieceType;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        MoveCalculator p;
        switch (type) {
            case KNIGHT:
                p = new KnightMoveCalculator(board, myPosition);
                break;
            case ROOK:
                p = new RookMoveCalculator(board, myPosition);
                break;
            case PAWN:
                p = new PawnMoveCalculator(board, myPosition);
                break;
            case KING:
                p = new KingMoveCalculator(board, myPosition);
                break;
            case BISHOP:
                p = new BishopMoveCalculator(board, myPosition);
                break;
            default:
                p = new QueenMoveCalculator(board, myPosition);
        }

        return p.calculateMoves();
    }

    @Override
    public String toString() {
        return String.format("[%s] type: %s, color: %s", getClass().getName(), this.type, this.color);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        ChessPiece other = (ChessPiece)obj;
        return (type == other.type && color == other.color);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + color.hashCode();
        return result;
    }
}
