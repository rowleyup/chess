package ui;

import model.AuthData;
import server.JsonUsage;
import server.ResponseException;
import websocket.NotificationHandler;
import websocket.messages.*;
import java.util.*;
import static ui.EscapeSequences.*;

public class InGameRepl implements NotificationHandler {
    protected final InGameClient client;
    protected final Scanner scanner;
    protected chess.ChessGame gameData;
    protected String team;
    protected boolean over;

    public InGameRepl(AuthData auth, String url) {
        client = new InGameClient(auth, this, url);
        scanner = new Scanner(System.in);
        over = false;
    }

    public void run(int game, String color) {
        this.team = color;
        client.setId(game);

        try {
            client.connect();
        } catch (ResponseException e) {
            String message = e.getMessage();
            System.out.print(SET_TEXT_BLINKING + SET_TEXT_BOLD + SET_TEXT_COLOR_RED + message + RESET_TEXT_BLINKING);
        }

        System.out.println(client.help());
        String result = "";

        while (!result.equals("leave") && !over) {
            printPrompt();
            String input = scanner.nextLine();
            var inputs = input.toLowerCase().split(" ");
            result = (inputs.length > 0) ? inputs[0] : "help";

            String response;

            try {
                switch (result) {
                    case "leave" -> response = client.leave();
                    case "redraw" -> response = drawBoard();
                    case "move" -> response = movePrompt();
                    case "highlight" -> response = highlightMove(getSquare());
                    case "resign" -> response = resignPrompt();
                    default -> response = client.help();
                }
                System.out.print(SET_TEXT_COLOR_GREEN + response);
            } catch (Throwable e) {
                String message = e.getMessage();
                System.out.print(SET_TEXT_BLINKING + SET_TEXT_BOLD + SET_TEXT_COLOR_RED + message + RESET_TEXT_BLINKING);
            }
        }

        if (over) {
            while (!result.equals("leave")) {
                printPrompt();
                String input = scanner.nextLine();
                var inputs = input.toLowerCase().split(" ");
                result = (inputs.length > 0) ? inputs[0] : "help";

                String response;

                try {
                    switch (result) {
                        case "leave" -> response = client.leave();
                        case "redraw" -> response = drawBoard();
                        default -> response = client.overHelp();
                    }
                    System.out.print(SET_TEXT_COLOR_GREEN + response);
                } catch (Throwable e) {
                    String message = e.getMessage();
                    System.out.print(SET_TEXT_BOLD + SET_TEXT_COLOR_RED + message);
                }
            }
        }
    }

    public void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + "[in game] >>> " + SET_TEXT_COLOR_BLUE);
    }

    @Override
    public void notify(String notification) {
        ServerMessage.ServerMessageType type = JsonUsage.fromJson(notification, ServerMessage.class).getServerMessageType();
        switch (type) {
            case ServerMessage.ServerMessageType.NOTIFICATION -> printUpdate(notification);
            case ServerMessage.ServerMessageType.LOAD_GAME -> printBoard(notification);
            default -> printError(notification);
        }
    }

    protected void printBoard(String notification) {
        ServerLoadMessage note = JsonUsage.fromJson(notification, ServerLoadMessage.class);
        this.gameData = note.getGame();
        if (note.isCheckMate()) {
            over = true;
        }

        if (note.getMessage() != null) {
            System.out.print("\n" + SET_TEXT_COLOR_YELLOW + note.getMessage());
        }
        System.out.print(SET_TEXT_COLOR_GREEN + drawBoard());
        printPrompt();
    }

    protected void printUpdate(String notification) {
        ServerNotifyMessage note = JsonUsage.fromJson(notification, ServerNotifyMessage.class);
        String message = note.getMessage();

        if (message.contains("OVER")) {
            over = true;
        }
        System.out.print("\n" + SET_TEXT_COLOR_YELLOW + message);
        printPrompt();
    }

    protected void printError(String notification) {
        ServerErrorMessage note = JsonUsage.fromJson(notification, ServerErrorMessage.class);
        System.out.print("\n" + SET_TEXT_BOLD + SET_TEXT_COLOR_RED + note.getErrorMessage());
        printPrompt();
    }

    protected String drawBoard() {
        if (team.equals("OBSERVER")) {
            return new BoardDrawer(gameData).drawWhite(new HashSet<>());
        }
        else if (team.equals("WHITE")) {
            return new BoardDrawer(gameData).drawWhite(new HashSet<>());
        }
        else {
            return new BoardDrawer(gameData).drawBlack(new HashSet<>());
        }
    }

    protected String resignPrompt() throws ResponseException {
        if (over) {
            return client.overHelp();
        }

        System.out.print("\n" + SET_TEXT_COLOR_BLUE + "Are you sure you want to resign? (Y/N) >>> " + SET_TEXT_COLOR_LIGHT_GREY);
        String input = scanner.nextLine();
        input = input.toUpperCase();
        switch (input) {
            case "Y", "YES" -> {
                client.resign();
                over = true;
                return "";
            }
            case "NO", "N" -> {
                return "Resignation canceled";
            }
            default -> {
                return resignPrompt();
            }
        }
    }

    protected String movePrompt() throws ResponseException {
        if (over) {
            return client.overHelp();
        }

        System.out.print(SET_TEXT_COLOR_BLUE + "Move from ");
        var from = getSquare();
        System.out.print(SET_TEXT_COLOR_BLUE + "Move to ");
        var to = getSquare();

        var piece = gameData.getBoard().getPiece(from);
        if (piece != null) {
            if (piece.getTeamColor() != chess.ChessGame.TeamColor.valueOf(team)) {
                throw new ResponseException("Error: Not your piece");
            }
            if (chess.ChessGame.TeamColor.valueOf(team) != gameData.getTeamTurn()) {
                throw new ResponseException("Error: Not your turn");
            }
            if (piece.getPieceType() == chess.ChessPiece.PieceType.PAWN) {
                if (piece.getTeamColor() == chess.ChessGame.TeamColor.BLACK && from.getRow() == 2) {
                    client.move(from, to, getPromote());
                }
                else if (piece.getTeamColor() == chess.ChessGame.TeamColor.WHITE && from.getRow() == 7) {
                    client.move(from, to, getPromote());
                }
            }
        }
        else {
            throw new ResponseException("Error: No piece");
        }

        client.move(from, to, null);
        return "";
    }

    protected chess.ChessPosition getSquare() throws ResponseException {
        var numStrings = new ArrayList<>(List.of("1", "2", "3", "4", "5", "6", "7", "8"));

        System.out.print(SET_TEXT_COLOR_BLUE + "Square (e.g. a1) >>> " + SET_TEXT_COLOR_LIGHT_GREY);
        String input = scanner.nextLine();
        var inputs = input.toLowerCase().split("");
        if (!numStrings.contains(inputs[1])) {
            throw new ResponseException("Error: Invalid coordinates");
        }
        int firstNum = Integer.parseInt(inputs[1]);

        int secondNum;
        switch (inputs[0]) {
            case "a" -> secondNum = 1;
            case "b" -> secondNum = 2;
            case "c" -> secondNum = 3;
            case "d" -> secondNum = 4;
            case "e" -> secondNum = 5;
            case "f" -> secondNum = 6;
            case "g" -> secondNum = 7;
            case "h" -> secondNum = 8;
            default -> throw new ResponseException("Error: Invalid coordinates");
        }

        return new chess.ChessPosition(firstNum, secondNum);
    }

    protected chess.ChessPiece.PieceType getPromote() {
        System.out.print("\n" + SET_TEXT_COLOR_BLUE + "Promote to >>> " + SET_TEXT_COLOR_LIGHT_GREY);
        String input = scanner.nextLine().toLowerCase();

        switch (input) {
            case "queen" -> {
                return chess.ChessPiece.PieceType.QUEEN;
            }
            case "bishop" -> {
                return chess.ChessPiece.PieceType.BISHOP;
            }
            case "rook" -> {
                return chess.ChessPiece.PieceType.ROOK;
            }
            case "knight" -> {
                return chess.ChessPiece.PieceType.KNIGHT;
            }
            default -> {
                System.out.print(SET_TEXT_COLOR_RED + "Options: queen, bishop, rook, knight");
                return getPromote();
            }
        }
    }

    protected String highlightMove(chess.ChessPosition pos) {
        if (over) {
            return client.overHelp();
        }

        Collection<chess.ChessMove> moves = gameData.validMoves(pos);

        if (team.equals("WHITE")) {
            return new BoardDrawer(gameData).drawWhite(moves);
        }
        else if (team.equals("BLACK")) {
            return new BoardDrawer(gameData).drawBlack(moves);
        }
        else {
            return new BoardDrawer(gameData).drawWhite(moves);
        }
    }
}
