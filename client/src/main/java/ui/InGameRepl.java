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

        while (!result.equals("quit") && !over) {
            printPrompt();
            String input = scanner.nextLine();
            var inputs = input.toLowerCase().split(" ");
            result = (inputs.length > 0) ? inputs[0] : "help";

            String response;

            try {
                switch (result) {
                    case "quit" -> response = "";
                    case "white" -> response = new BoardDrawer(gameData).drawWhite();
                    case "black" -> response = new BoardDrawer(gameData).drawBlack();
                    default -> response = client.help();
                }
                System.out.print(SET_TEXT_COLOR_GREEN + response);
            } catch (Throwable e) {
                String message = e.getMessage();
                System.out.print(SET_TEXT_BLINKING + SET_TEXT_BOLD + SET_TEXT_COLOR_RED + message + RESET_TEXT_BLINKING);
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

        if (team == null) {
            new BoardDrawer(gameData).drawWhite();
        }
        else if (team.equals("white")) {
            new BoardDrawer(gameData).drawWhite();
        }
        else {
            new BoardDrawer(gameData).drawBlack();
        }

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
}
