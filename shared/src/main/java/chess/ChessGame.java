package chess;

import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor turn;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        setTeamTurn(TeamColor.WHITE);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (board.getPiece(startPosition) == null) {
            return null;
        }
        return board.getPiece(startPosition).pieceMoves(board, startPosition);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece oldPiece = board.getPiece(move.getStartPosition());
        if (oldPiece == null) {
            throw new InvalidMoveException("No piece found at position");
        }
        if (oldPiece.getTeamColor() != turn) {
            throw new InvalidMoveException(String.format("%s piece cannot be moved on %s turn", oldPiece.getTeamColor(), turn));
        }

        Collection<ChessMove> moves = validMoves(move.getStartPosition());
        boolean valid = false;
        if (!moves.isEmpty()) {
            for (ChessMove m : moves) {
                if (m.equals(move)) {
                    valid = true;
                    break;
                }
            }
        }

        if (valid) {
            board.addPiece(move.getStartPosition(), null);
            ChessPiece beatenPiece = board.getPiece(move.getEndPosition());
            if (move.getPromotionPiece() != null) {
                ChessPiece newPiece = new ChessPiece(oldPiece.getTeamColor(), move.getPromotionPiece());
                board.addPiece(move.getEndPosition(), newPiece);
            }
            else {
                board.addPiece(move.getEndPosition(), oldPiece);
            }
            if (turn == TeamColor.BLACK) {
                if (isInCheck(TeamColor.BLACK)) {
                    undoMove(move, beatenPiece);
                    throw new InvalidMoveException("Leaves king in check");
                }
            }
            else {
                if (isInCheck(TeamColor.WHITE)) {
                    undoMove(move, beatenPiece);
                    throw new InvalidMoveException("Leaves king in check");
                }
            }
        }
        else {
            throw new InvalidMoveException();
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }
        boolean stalemate = true;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition p = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(p);
                if (piece == null) {
                    continue;
                }
                if (piece.getTeamColor() != teamColor) {
                    continue;
                }
                Collection<ChessMove> moves = piece.pieceMoves(board, p);
                if (!moves.isEmpty()) {
                    stalemate = false;
                    j = 9;
                    i = 9;
                }
            }
        }
        return stalemate;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    private void undoMove(ChessMove move, ChessPiece oldPiece) {
        ChessPiece movedPiece = board.getPiece(move.getEndPosition());
        if (move.getPromotionPiece() != null) {
            board.addPiece(move.getStartPosition(), new ChessPiece(movedPiece.getTeamColor(), ChessPiece.PieceType.PAWN));
        }
        else {
            board.addPiece(move.getStartPosition(), movedPiece);
        }
        board.addPiece(move.getEndPosition(), oldPiece);
    }
}
