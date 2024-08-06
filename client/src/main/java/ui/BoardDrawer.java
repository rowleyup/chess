package ui;

import static ui.EscapeSequences.*;

public class BoardDrawer {
    private final chess.ChessGame game;

    public BoardDrawer(chess.ChessGame game) {
        this.game = game;
    }

    public String drawWhite() {
        String top = SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "    a" + EMPTY;
        top = top + " b" + EMPTY + " c" + EMPTY + " d" + EMPTY + " e" + EMPTY + " f" + EMPTY + " g" + EMPTY;
        top = top + " h" + EMPTY + "   ";
        top = top + RESET_BG_COLOR + RESET_TEXT_COLOR + "\n";
        StringBuilder picture = new StringBuilder(top);
        for (int i = 8; i >= 1; i--) {
            picture.append(boardTemplate(String.valueOf(i), 1));
        }

        picture.append(top);
        return picture.toString();
    }

    public String drawBlack() {
        String top = SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "    h" + EMPTY;
        top = top + " g" + EMPTY + " f" + EMPTY + " e" + EMPTY + " d" + EMPTY + " c" + EMPTY;
        top = top + " b" + EMPTY + " a" + EMPTY + "   ";
        top = top + RESET_BG_COLOR + RESET_TEXT_COLOR + "\n";
        StringBuilder picture = new StringBuilder(top);
        for (int i = 1; i <= 8; i++) {
            picture.append(boardTemplate(String.valueOf(i), 0));
        }

        picture.append(top);
        return picture.toString();
    }

    private String boardTemplate(String num, int direction) {
        chess.ChessBoard board = game.getBoard();
        StringBuilder pic = new StringBuilder();
        pic.append(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        pic.append(" ").append(num).append(" ");

        if (direction == 1) {
            for (int i = 1; i <= 8; i++) {
                pic.append(square(i, num, board));
            }
        }
        else {
            for (int i = 8; i >= 1; i--) {
                pic.append(square(i, num, board));
            }
        }

        pic.append(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        pic.append(" ").append(num).append(" ").append(RESET_BG_COLOR).append("\n");
        return pic.toString();
    }

    private String square(int i, String num, chess.ChessBoard board) {
        StringBuilder pic = new StringBuilder();
        if ((Integer.parseInt(num) % 2 != 0 && i % 2 != 0) || (Integer.parseInt(num) % 2 == 0 && i % 2 == 0)) {
            pic.append(SET_BG_COLOR_DARK_GREY).append(" ");
        } else {
            pic.append(SET_BG_COLOR_WHITE).append(" ");
        }

        chess.ChessPiece piece = board.getPiece(new chess.ChessPosition(Integer.parseInt(num), i));
        if (piece == null) {
            pic.append(EMPTY).append(" ");
        } else {
            switch (piece.getPieceType()) {
                case chess.ChessPiece.PieceType.KING -> pic.append(addKing(piece));
                case chess.ChessPiece.PieceType.QUEEN -> pic.append(addQueen(piece));
                case chess.ChessPiece.PieceType.BISHOP -> pic.append(addBishop(piece));
                case chess.ChessPiece.PieceType.KNIGHT -> pic.append(addKnight(piece));
                case chess.ChessPiece.PieceType.ROOK -> pic.append(addRook(piece));
                default -> pic.append(addPawn(piece));
            }
        }

        return pic.toString();
    }

    private String addKing(chess.ChessPiece piece) {
        if (piece.getTeamColor() == chess.ChessGame.TeamColor.WHITE) {
            return WHITE_KING + " ";
        }
        else {
            return BLACK_KING + " ";
        }
    }

    private String addQueen(chess.ChessPiece piece) {
        if (piece.getTeamColor() == chess.ChessGame.TeamColor.WHITE) {
            return WHITE_QUEEN + " ";
        }
        else {
            return BLACK_QUEEN + " ";
        }
    }

    private String addBishop(chess.ChessPiece piece) {
        if (piece.getTeamColor() == chess.ChessGame.TeamColor.WHITE) {
            return WHITE_BISHOP + " ";
        }
        else {
            return BLACK_BISHOP + " ";
        }
    }

    private String addKnight(chess.ChessPiece piece) {
        if (piece.getTeamColor() == chess.ChessGame.TeamColor.WHITE) {
            return WHITE_KNIGHT + " ";
        }
        else {
            return BLACK_KNIGHT + " ";
        }
    }

    private String addRook(chess.ChessPiece piece) {
        if (piece.getTeamColor() == chess.ChessGame.TeamColor.WHITE) {
            return WHITE_ROOK + " ";
        }
        else {
            return BLACK_ROOK + " ";
        }
    }

    private String addPawn(chess.ChessPiece piece) {
        if (piece.getTeamColor() == chess.ChessGame.TeamColor.WHITE) {
            return WHITE_PAWN + " ";
        }
        else {
            return BLACK_PAWN + " ";
        }
    }
}
