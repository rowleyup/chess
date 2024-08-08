package ui;

import model.AuthData;
import server.ResponseException;
import server.ServerFacade;
import websocket.NotificationHandler;
import websocket.messages.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class InGameRepl implements NotificationHandler {
    private final InGameClient client;
    private final Scanner scanner;
    private chess.ChessGame gameData;
    private String team;
    private boolean over;

    public InGameRepl(ServerFacade server, AuthData auth, String url) {
        client = new InGameClient(server, auth, this, url);
        scanner = new Scanner(System.in);
        over = false;
    }

    public void run(int game, String color) {
        this.team = color;
        client.setId(game);
        client.setTeam(color);

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
                    case "move" -> {
                        if (!over) {response = movePrompt();}
                        else {response = client.overHelp();}
                    }
                    case "highlight" -> {
                        if (!over) {response = client.highlightMove(getSquare());}
                        else {response = client.overHelp();}
                    }
                    case "resign" -> {
                        if (!over) {response = resignPrompt();}
                        else {response = client.overHelp();}
                    }
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
    public void notify(ServerMessage notification) {
        ServerMessage.ServerMessageType type = notification.getServerMessageType();
        switch (type) {
            case ServerMessage.ServerMessageType.NOTIFICATION -> printUpdate(notification);
            case ServerMessage.ServerMessageType.LOAD_GAME -> printBoard(notification);
            default -> printError(notification);
        }
    }

    private void printBoard(ServerMessage notification) {
        ServerLoadMessage note = (ServerLoadMessage) notification;
        this.gameData = note.getGame();
        if (note.isCheckMate()) {
            System.out.print("\n" + SET_TEXT_COLOR_YELLOW + "GAME OVER: You are in checkmate");
            over = true;
        }
        else if (note.isCheck()) {
            System.out.print("\n" + SET_TEXT_COLOR_YELLOW + "WARNING: You are in check");
        }

        System.out.print(SET_TEXT_COLOR_GREEN + drawBoard());
        printPrompt();
    }

    private void printUpdate(ServerMessage notification) {
        ServerMoveMessage note = (ServerMoveMessage) notification;
        String message = note.getMessage();

        if (message.contains("OVER")) {
            over = true;
        }
        System.out.print("\n" + SET_TEXT_COLOR_YELLOW + message);
        printPrompt();
    }

    private void printError(ServerMessage notification) {
        ServerErrorMessage note = (ServerErrorMessage) notification;
        System.out.print("\n" + SET_TEXT_BOLD + SET_TEXT_COLOR_RED + note.getErrorMessage());
        printPrompt();
    }

    private String drawBoard() {
        if (team == null) {
            return new BoardDrawer(gameData).drawWhite();
        }
        else if (team.equals("white")) {
            return new BoardDrawer(gameData).drawWhite();
        }
        else {
            return new BoardDrawer(gameData).drawBlack();
        }
    }

    private String resignPrompt() {
        System.out.print("\n" + SET_TEXT_COLOR_BLUE + "Are you sure you want to resign? (Y/N) >>> " + SET_TEXT_COLOR_LIGHT_GREY);
        String input = scanner.nextLine();
        input = input.toUpperCase();
        switch (input) {
            case "Y", "YES" -> {
                String m = client.resign();
                over = true;
                return m;
            }
            case "NO", "N" -> {
                return "Resignation canceled";
            }
            default -> {
                return resignPrompt();
            }
        }
    }

    private String movePrompt() throws ResponseException {
        System.out.print(SET_TEXT_COLOR_BLUE + "Move from ");
        var from = getSquare();
        System.out.print(SET_TEXT_COLOR_BLUE + "Move to ");
        var to = getSquare();

        return client.move(from, to);
    }

    private chess.ChessPosition getSquare() throws ResponseException {
        var numbers = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8));

        System.out.print(SET_TEXT_COLOR_BLUE + "Square (e.g. a1) >>> " + SET_TEXT_COLOR_LIGHT_GREY);
        String input = scanner.nextLine();
        var inputs = input.toLowerCase().split("");
        int secondNum = Integer.parseInt(inputs[1]);
        if (!numbers.contains(secondNum)) {
            throw new ResponseException("Error: Invalid coordinates");
        }

        int firstNum;
        switch (inputs[0]) {
            case "a" -> firstNum = 1;
            case "b" -> firstNum = 2;
            case "c" -> firstNum = 3;
            case "d" -> firstNum = 4;
            case "e" -> firstNum = 5;
            case "f" -> firstNum = 6;
            case "g" -> firstNum = 7;
            case "h" -> firstNum = 8;
            default -> throw new ResponseException("Error: Invalid coordinates");
        }

        return new chess.ChessPosition(firstNum, secondNum);
    }
}
