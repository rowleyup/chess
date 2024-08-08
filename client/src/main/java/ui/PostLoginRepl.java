package ui;

import model.AuthData;
import model.GameData;
import server.ResponseException;
import server.ServerFacade;
import java.util.ArrayList;
import java.util.Scanner;
import static ui.EscapeSequences.*;
import static ui.EscapeSequences.RESET_TEXT_BLINKING;

public class PostLoginRepl {
    private final PostLoginClient client;
    private final Scanner scanner;
    private GameData game;
    private String color;
    private final AuthData userAuth;
    private boolean success;
    private final String url;

    public PostLoginRepl(ServerFacade server, AuthData auth, String url) {
        this.url = url;
        userAuth = auth;
        client = new PostLoginClient(server, userAuth);
        scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println(client.help());
        String result = "";

        while (!result.equals("logout")) {
            success = true;
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
            } catch (Throwable e) {
                String message = e.getMessage();
                System.out.print(SET_TEXT_BLINKING + SET_TEXT_BOLD + SET_TEXT_COLOR_RED + message + RESET_TEXT_BLINKING);
                success = false;
            }

            if (success && result.equals("join")) {
                var inGame = new InGameRepl(userAuth, url);
                inGame.run(game.gameID(), color);
            }
            else if (success && result.equals("observe")) {
                var inGame = new ObserveGameRepl(userAuth, url);
                inGame.run(game.gameID(), "OBSERVER");
            }
        }
    }

    public void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + "[logged in] >>> " + SET_TEXT_COLOR_BLUE);
    }

    private String createPrompt() throws ResponseException {
        System.out.print("\n" + SET_TEXT_COLOR_BLUE + "GAME NAME >>> " + SET_TEXT_COLOR_LIGHT_GREY);
        String name = scanner.nextLine();
        if (name.length() > 12) {
            success = false;
            return SET_TEXT_COLOR_RED + "ERROR: game name must be less than 12 characters";
        }

        client.create(name);
        return String.format("Created game: %s", name);
    }

    private String observePrompt() {
        int gameId;
        try {
            gameId = getId();
        } catch (ResponseException e) {
            success = false;
            return SET_TEXT_COLOR_RED + "ERROR: invalid game id";
        }

        game = client.observe(gameId);
        return String.format("Observing game: %s", game.gameName());
    }

    private String joinPrompt() throws ResponseException {
        int gameId;
        try {
            gameId = getId();
        } catch (ResponseException e) {
            success = false;
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
            success = false;
            return SET_TEXT_COLOR_RED + "ERROR: invalid color";
        }

        switch (color) {
            case "w", "white": this.color = "WHITE";
            case "b", "black": this.color = "BLACK";
        }

        game = client.join(gameId, color);
        return String.format("Joining game: %s", game.gameName());
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
