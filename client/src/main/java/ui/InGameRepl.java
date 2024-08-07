package ui;

import model.AuthData;
import server.ServerFacade;
import websocket.NotificationHandler;
import websocket.messages.*;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class InGameRepl implements NotificationHandler {
    private final InGameClient client;
    private final Scanner scanner;
    private chess.ChessGame gameData;
    private String team;
    private boolean over;

    public InGameRepl(ServerFacade server, AuthData auth) {
        client = new InGameClient(server, auth);
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
                        if (!over) {response = client.move();}
                        else {response = client.overHelp();}
                    }
                    case "highlight" -> {
                        if (!over) {response = highlightMoves();}
                        else {response = client.overHelp();}
                    }
                    case "resign" -> {
                        if (!over) {response = resign();}
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
}
