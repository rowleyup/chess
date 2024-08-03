package ui;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class PreLoginRepl implements Repl{
    private final ChessClient client;

    public PreLoginRepl(String url) {
        client = new PreLoginClient(url);
    }

    @Override
    public void run() {
        System.out.println(SET_TEXT_UNDERLINE + SET_TEXT_COLOR_MAGENTA + "Welcome to Chess! Sign in to start." + RESET_TEXT_UNDERLINE);
        System.out.println(client.help());

        Scanner scanner = new Scanner(System.in);
        String result = "";

        while (!result.equals("quit")) {
            printPrompt();
            String inLine = scanner.nextLine();

            try {
                result = client.eval(inLine);
                System.out.print(SET_TEXT_COLOR_GREEN + result);
            } catch (Throwable e) {
                String message = e.toString();
                System.out.print(SET_TEXT_BLINKING + SET_TEXT_BOLD + SET_TEXT_COLOR_RED + message + RESET_TEXT_BLINKING);
            }
        }
    }

    @Override
    public void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + "[pre-login] >>> " + SET_TEXT_COLOR_BLUE);
    }
}
