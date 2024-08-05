package ui;

import server.ResponseException;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class PreLoginRepl {
    private final PreLoginClient client;
    private final PostLoginRepl postLogin;
    private final Scanner scanner;

    public PreLoginRepl(String url) {
        postLogin = new PostLoginRepl(url);
        client = new PreLoginClient(url, postLogin.client());
        scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println(SET_TEXT_UNDERLINE + SET_TEXT_COLOR_MAGENTA + "Welcome to Chess! Sign in to start." + RESET_TEXT_UNDERLINE);
        System.out.println(client.help());
        String result = "";

        while (!result.equals("quit")) {
            printPrompt();
            String inLine = scanner.nextLine();
            String response;

            try {
                result = client.eval(inLine);

                switch (result) {
                    case "quit" -> response = "";
                    case "login" -> response = loginPrompt();
                    case "register" -> response = registerPrompt();
                    default -> response = result;
                }
                System.out.print(SET_TEXT_COLOR_GREEN + response);

                if (result.equals("login") || result.equals("register")) {
                    postLogin.run();
                }
            } catch (Throwable e) {
                String message = e.toString();
                System.out.print(SET_TEXT_BLINKING + SET_TEXT_BOLD + SET_TEXT_COLOR_RED + message + RESET_TEXT_BLINKING);
            }
        }
    }

    public void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + "[logged out] >>> " + SET_TEXT_COLOR_BLUE);
    }

    private String loginPrompt() throws ResponseException {
        String username;
        String password;
        try {
            username = usernamePrompt();
            password = passwordPrompt();
        } catch (ResponseException e) {
            return SET_TEXT_COLOR_RED + "ERROR: invalid username";
        }

        client.login(username, password);

        return "Logging in user: " + username;
    }

    private String registerPrompt() throws ResponseException {
        String username;
        String password;
        try {
            username = usernamePrompt();
            password = passwordPrompt();
        } catch (ResponseException e) {
            return SET_TEXT_COLOR_RED + "ERROR: invalid username";
        }

        System.out.print("\n" + SET_TEXT_COLOR_BLUE + "EMAIL >>> " + SET_TEXT_COLOR_LIGHT_GREY);
        String email = scanner.nextLine();
        if (!email.contains("@")) {
            return SET_TEXT_COLOR_RED + "ERROR: invalid email";
        }

        client.register(username, password, email);
        return "Registering and logging in user: " + username;
    }

    private String usernamePrompt() throws ResponseException {
        System.out.print("\n" + SET_TEXT_COLOR_BLUE + "USERNAME >>> " + SET_TEXT_COLOR_LIGHT_GREY);
        String username = scanner.nextLine();
        if (username.contains(" ")) {
            throw new ResponseException("");
        }
        return username;
    }

    private String passwordPrompt() {
        System.out.print("\n" + SET_TEXT_COLOR_BLUE + "PASSWORD >>> " + SET_TEXT_COLOR_LIGHT_GREY);
        return scanner.nextLine();
    }
}
