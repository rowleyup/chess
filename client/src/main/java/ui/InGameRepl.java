package ui;

import server.ServerFacade;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class InGameRepl {
    private final InGameClient client;
    private final Scanner scanner;
    private final chess.ChessGame gameData;

    public InGameRepl(ServerFacade server) {
        client = new InGameClient(server);
        scanner = new Scanner(System.in);
        gameData = new chess.ChessGame();
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
}
