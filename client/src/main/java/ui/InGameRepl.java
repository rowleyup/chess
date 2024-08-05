package ui;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class InGameRepl {
    private final InGameClient client;
    private final Scanner scanner;
    private final chess.ChessGame game;

    public InGameRepl(String url) {
        client = new InGameClient(url);
        scanner = new Scanner(System.in);
        game = new chess.ChessGame();
    }

    public InGameClient client() {
        return client;
    }

    public void run(int game, String color) {
        client.setId(game);
        client.setTeam(color);

        System.out.println(client.help());
        String result = "";

        while (!result.equals("quit")) {
            printPrompt();
            String input = scanner.nextLine();
            var inputs = input.toLowerCase().split(" ");
            result = (inputs.length > 0) ? inputs[0] : "help";

            String response;

            try {
                switch (result) {
                    case "quit" -> response = "";
                    case "white" -> response = drawWhite();
                    case "black" -> response = drawBlack();
                    default -> response = client.help();
                }
                System.out.print(SET_TEXT_COLOR_GREEN + response);
            } catch (Throwable e) {
                String message = e.toString();
                System.out.print(SET_TEXT_BLINKING + SET_TEXT_BOLD + SET_TEXT_COLOR_RED + message + RESET_TEXT_BLINKING);
            }
        }
    }

    public void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + "[in game] >>> " + SET_TEXT_COLOR_BLUE);
    }

    private String drawWhite() {
        String top = SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " a  b  c  d  e  f  g  h " + RESET_BG_COLOR + RESET_TEXT_COLOR + "\n";
        StringBuilder picture = new StringBuilder(top);
        for (int i = 8; i >= 1; i--) {
            picture.append(boardTemplate(String.valueOf(i), 1));
        }

        picture.append(top);
        return picture.toString();
    }

    private String drawBlack() {
        String top = SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " h  g  f  e  d  c  b  a " + RESET_BG_COLOR + RESET_TEXT_COLOR + "\n";
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
        pic.append(" ").append(num).append(" ").append("\n");
        return pic.toString();
    }

    private String square(int i, String num, chess.ChessBoard board) {
        StringBuilder pic = new StringBuilder();
        if (i % 2 != 0) {
            pic.append(SET_BG_COLOR_WHITE).append(" ");
        } else {
            pic.append(SET_BG_COLOR_BLACK).append(" ");
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
            return " " + WHITE_KING + " ";
        }
        else {
            return " " + BLACK_KING + " ";
        }
    }

    private String addQueen(chess.ChessPiece piece) {
        if (piece.getTeamColor() == chess.ChessGame.TeamColor.WHITE) {
            return " " + WHITE_QUEEN + " ";
        }
        else {
            return " " + BLACK_QUEEN + " ";
        }
    }

    private String addBishop(chess.ChessPiece piece) {
        if (piece.getTeamColor() == chess.ChessGame.TeamColor.WHITE) {
            return " " + WHITE_BISHOP + " ";
        }
        else {
            return " " + BLACK_BISHOP + " ";
        }
    }

    private String addKnight(chess.ChessPiece piece) {
        if (piece.getTeamColor() == chess.ChessGame.TeamColor.WHITE) {
            return " " + WHITE_KNIGHT + " ";
        }
        else {
            return " " + BLACK_KNIGHT + " ";
        }
    }

    private String addRook(chess.ChessPiece piece) {
        if (piece.getTeamColor() == chess.ChessGame.TeamColor.WHITE) {
            return " " + WHITE_ROOK + " ";
        }
        else {
            return " " + BLACK_ROOK + " ";
        }
    }

    private String addPawn(chess.ChessPiece piece) {
        if (piece.getTeamColor() == chess.ChessGame.TeamColor.WHITE) {
            return " " + WHITE_PAWN + " ";
        }
        else {
            return " " + BLACK_PAWN + " ";
        }
    }
}
