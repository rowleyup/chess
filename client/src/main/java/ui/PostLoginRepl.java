package ui;

import server.ResponseException;

import java.util.ArrayList;
import java.util.Scanner;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.RESET_TEXT_BLINKING;

public class PostLoginRepl {
    private final PostLoginClient client;
    private final InGameRepl inGame;
    private final Scanner scanner;
    private int game = 0;
    private String color;

    public PostLoginRepl(String url) {
        inGame = new InGameRepl(url);
        client = new PostLoginClient(url, inGame.client());
        scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println(client.help());
        String result = "";

        while (!result.equals("logout")) {
            printPrompt();
            String input = scanner.nextLine();
            var inputs = input.toLowerCase().split(" ");
            result = (inputs.length > 0) ? inputs[0] : "help";

            String response;

            try {
                switch (result) {
                    case "logout" -> response = client.logout();
                    case "create" -> response = createPrompt();
                    case "list" -> response = client.list();
                    case "join" -> response = joinPrompt();
                    case "observe" -> response = observePrompt();
                    default -> response = client.help();
                }
                System.out.print(SET_TEXT_COLOR_GREEN + response);

                if (result.equals("join")) {
                    inGame.run(game, color);
                }
                else if (result.equals("observe")) {
                    inGame.run(game, null);
                }
            } catch (Throwable e) {
                String message = e.toString();
                System.out.print(SET_TEXT_BLINKING + SET_TEXT_BOLD + SET_TEXT_COLOR_RED + message + RESET_TEXT_BLINKING);
            }
        }
    }

    public void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + "[logged in] >>> " + SET_TEXT_COLOR_BLUE);
    }

    public PostLoginClient client() {
        return client;
    }

    private String createPrompt() {
        System.out.print("\n" + SET_TEXT_COLOR_BLUE + "GAME NAME >>> " + SET_TEXT_COLOR_LIGHT_GREY);
        String name = scanner.nextLine();
        client.create(name);
        return String.format("Created game: %s", name);
    }

    private String observePrompt() {
        int gameId;
        try {
            gameId = getId();
        } catch (ResponseException e) {
            return SET_TEXT_COLOR_RED + "ERROR: invalid game id";
        }

        game = client.observe(gameId);
        return String.format("Observing game: %s", gameId);
    }

    private String joinPrompt() {
        int gameId;
        try {
            gameId = getId();
        } catch (ResponseException e) {
            return SET_TEXT_COLOR_RED + "ERROR: invalid game id";
        }

        System.out.print("\n" + SET_TEXT_COLOR_BLUE + "PLAYER COLOR >>> " + SET_TEXT_COLOR_LIGHT_GREY);
        String color = scanner.nextLine().toLowerCase();

        var possible = new ArrayList<String>();
        possible.add("w");
        possible.add("white");
        possible.add("b");
        possible.add("black");
        if (!possible.contains(color)) {
            return SET_TEXT_COLOR_RED + "ERROR: invalid color";
        }
        this.color = color;

        game = client.join(gameId, color);
        return String.format("Joining game: %s", gameId);
    }

    private int getId() throws ResponseException {
        System.out.print("\n" + SET_TEXT_COLOR_BLUE + "GAME ID >>> " + SET_TEXT_COLOR_LIGHT_GREY);
        String input = scanner.nextLine();
        if (input.equals("\n") || input.isEmpty()) {
            throw new ResponseException("");
        }

        int gameId;
        try {
            gameId = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new ResponseException("");
        }
        return gameId;
    }
}
