package chess;

import java.util.Collection;
import java.util.HashSet;

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
        ChessPiece oldPiece = board.getPiece(startPosition);
        if (oldPiece == null) {
            return null;
        }
        Collection<ChessMove> moves = oldPiece.pieceMoves(board, startPosition);
        if (moves == null) {
            return null;
        }
        Collection<ChessMove> validMoves = new HashSet<>();
        for (ChessMove m : moves) {
            if (!checkDanger(m)) {
                validMoves.add(m);
            }
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (board.getPiece(move.getStartPosition()) == null) {
            throw new InvalidMoveException("No piece at this location");
        }
        if (board.getPiece(move.getStartPosition()).getTeamColor() != getTeamTurn()) {
            throw new InvalidMoveException();
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
            movePiece(move);
            if (getTeamTurn() == TeamColor.BLACK) {
                setTeamTurn(TeamColor.WHITE);
            }
            else {
                setTeamTurn(TeamColor.BLACK);
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
        ChessPosition king = null;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition p = new ChessPosition(i, j);
                if (board.getPiece(p) == null) {
                    continue;
                }
                if (board.getPiece(p).getTeamColor() != teamColor) {
                    continue;
                }
                if (board.getPiece(p).getPieceType() == ChessPiece.PieceType.KING) {
                    king = p;
                    break;
                }
            }
        }
        if (king == null) {
            return false;
        }

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition p = new ChessPosition(i, j);
                if (board.getPiece(p) == null) {
                    continue;
                }
                if (board.getPiece(p).getTeamColor() == teamColor) {
                    continue;
                }
                Collection<ChessMove> moves = board.getPiece(p).pieceMoves(board, p);
                for (ChessMove m : moves) {
                    if (m.getEndPosition().equals(king)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 8; j++) {
                    ChessPosition p = new ChessPosition(i, j);
                    ChessPiece piece = board.getPiece(p);
                    if (piece == null) {
                        continue;
                    }
                    if (piece.getTeamColor() == teamColor) {
                        Collection<ChessMove> moves = validMoves(p);
                        if (!moves.isEmpty()) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        return false;
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
        return findStalemate(teamColor);
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

    private boolean findStalemate(TeamColor teamColor) {
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
                Collection<ChessMove> moves = validMoves(p);
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
     * Checks if the given move will cause the team's king to be in check
     *
     * @param move the move to check
     * @return false if safe, true if dangerous
     */
    private boolean checkDanger(ChessMove move) {
        ChessPiece beatenPiece = board.getPiece(move.getEndPosition());
        ChessPiece starterPiece = board.getPiece(move.getStartPosition());
        movePiece(move);
        if (isInCheck(starterPiece.getTeamColor())) {
            undoMove(move, beatenPiece);
            return true;
        }
        undoMove(move, beatenPiece);
        return false;
    }

    private void movePiece(ChessMove move) {
        ChessPiece oldPiece = board.getPiece(move.getStartPosition());
        board.addPiece(move.getStartPosition(), null);
        if (move.getPromotionPiece() != null) {
            ChessPiece newPiece = new ChessPiece(oldPiece.getTeamColor(), move.getPromotionPiece());
            board.addPiece(move.getEndPosition(), newPiece);
        }
        else {
            board.addPiece(move.getEndPosition(), oldPiece);
        }
    }
}
