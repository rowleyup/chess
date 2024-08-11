package ui;

import model.AuthData;
import server.ResponseException;
import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_RED;

public class ObserveGameRepl extends InGameRepl {
    public ObserveGameRepl(AuthData auth, String url) {
        super(auth, url);
    }

    @Override
    public void run(int game, String color) {
        this.team = color;
        client.setId(game);

        try {
            client.connect();
        } catch (ResponseException e) {
            String message = e.getMessage();
            System.out.print(SET_TEXT_BOLD + SET_TEXT_COLOR_RED + message);
        }

        System.out.println(client.help());
        String result = "";

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
                    case "highlight" -> response = highlightMove(getSquare());
                    default -> response = client.observeHelp();
                }
                System.out.print(SET_TEXT_COLOR_GREEN + response);
            } catch (Throwable e) {
                String message = e.getMessage();
                System.out.print(SET_TEXT_BOLD + SET_TEXT_COLOR_RED + message);
            }
        }
    }
}
