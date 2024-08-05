package ui;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class InGameRepl {
    private final InGameClient client;
    private final Scanner scanner;

    public InGameRepl(String url) {
        client = new InGameClient(url);
        scanner = new Scanner(System.in);
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

    private String drawWhite() {}

    private String drawBlack() {}
}
