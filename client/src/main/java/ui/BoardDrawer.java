package ui;

import chess.*;
import java.util.Collection;
import java.util.HashSet;
import static ui.EscapeSequences.*;

public class BoardDrawer {
    private final ChessGame game;

    public BoardDrawer(ChessGame game) {
        this.game = game;
    }

    public String drawWhite(Collection<ChessMove> moves) {
        String top = SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_WHITE + "    a" + EMPTY;
        top = top + " b" + EMPTY + " c" + EMPTY + " d" + EMPTY + " e" + EMPTY + " f" + EMPTY + " g" + EMPTY;
        top = top + " h" + EMPTY + "   ";
        top = top + RESET_BG_COLOR + RESET_TEXT_COLOR + "\n";
        StringBuilder picture = new StringBuilder("\n");
        picture.append(top);

        for (int i = 8; i >= 1; i--) {
            picture.append(boardTemplate(String.valueOf(i), 1, getPosList(moves)));
        }

        picture.append(top);
        return picture.toString();
    }

    public String drawBlack(Collection<ChessMove> moves) {
        String top = SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_WHITE + "    h" + EMPTY;
        top = top + " g" + EMPTY + " f" + EMPTY + " e" + EMPTY + " d" + EMPTY + " c" + EMPTY;
        top = top + " b" + EMPTY + " a" + EMPTY + "   ";
        top = top + RESET_BG_COLOR + RESET_TEXT_COLOR + "\n";
        StringBuilder picture = new StringBuilder("\n");
        picture.append(top);

        for (int i = 1; i <= 8; i++) {
            picture.append(boardTemplate(String.valueOf(i), 0, getPosList(moves)));
        }

        picture.append(top);
        return picture.toString();
    }

    private String boardTemplate(String num, int direction, Collection<ChessPosition> moves) {
        ChessBoard board = game.getBoard();
        StringBuilder pic = new StringBuilder();
        pic.append(SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_WHITE);
        pic.append(" ").append(num).append(" ").append(SET_TEXT_COLOR_BLACK);

        if (direction == 1) {
            for (int i = 1; i <= 8; i++) {
                if (moves.contains(new ChessPosition(Integer.parseInt(num), i))) {
                    pic.append(square(i, num, board, true));
                }
                else {
                    pic.append(square(i, num, board, false));
                }
            }
        }
        else {
            for (int i = 8; i >= 1; i--) {
                if (moves.contains(new ChessPosition(Integer.parseInt(num), i))) {
                    pic.append(square(i, num, board, true));
                }
                else {
                    pic.append(square(i, num, board, false));
                }
            }
        }

        pic.append(SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_WHITE);
        pic.append(" ").append(num).append(" ").append(RESET_BG_COLOR).append("\n");
        return pic.toString();
    }

    private String square(int i, String num, ChessBoard board, boolean highlight) {
        StringBuilder pic = new StringBuilder();
        if ((Integer.parseInt(num) % 2 != 0 && i % 2 != 0) || (Integer.parseInt(num) % 2 == 0 && i % 2 == 0)) {
            if (highlight) {
                pic.append(SET_BG_COLOR_DARK_GREEN).append(" ");
            }
            else {
                pic.append(SET_BG_COLOR_LIGHT_GREY).append(" ");
            }
        } else {
            if (highlight) {
                pic.append(SET_BG_COLOR_GREEN).append(" ");
            }
            else {
                pic.append(SET_BG_COLOR_WHITE).append(" ");
            }
        }

        ChessPiece piece = board.getPiece(new ChessPosition(Integer.parseInt(num), i));
        if (piece == null) {
            pic.append(EMPTY).append(" ");
        } else {
            switch (piece.getPieceType()) {
                case ChessPiece.PieceType.KING -> pic.append(addKing(piece));
                case ChessPiece.PieceType.QUEEN -> pic.append(addQueen(piece));
                case ChessPiece.PieceType.BISHOP -> pic.append(addBishop(piece));
                case ChessPiece.PieceType.KNIGHT -> pic.append(addKnight(piece));
                case ChessPiece.PieceType.ROOK -> pic.append(addRook(piece));
                default -> pic.append(addPawn(piece));
            }
        }

        return pic.toString();
    }

    private String addKing(ChessPiece piece) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return WHITE_KING + " ";
        }
        else {
            return BLACK_KING + " ";
        }
    }

    private String addQueen(ChessPiece piece) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return WHITE_QUEEN + " ";
        }
        else {
            return BLACK_QUEEN + " ";
        }
    }

    private String addBishop(ChessPiece piece) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return WHITE_BISHOP + " ";
        }
        else {
            return BLACK_BISHOP + " ";
        }
    }

    private String addKnight(ChessPiece piece) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return WHITE_KNIGHT + " ";
        }
        else {
            return BLACK_KNIGHT + " ";
        }
    }

    private String addRook(ChessPiece piece) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return WHITE_ROOK + " ";
        }
        else {
            return BLACK_ROOK + " ";
        }
    }

    private String addPawn(ChessPiece piece) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return WHITE_PAWN + " ";
        }
        else {
            return BLACK_PAWN + " ";
        }
    }

    private Collection<chess.ChessPosition> getPosList(Collection<chess.ChessMove> moves) {
        var pos = new HashSet<ChessPosition>();
        if (moves != null) {
            for (ChessMove move : moves) {
                pos.add(move.getEndPosition());
            }
        }
        return pos;
    }
}
