package ui;

import model.AuthData;
import model.UserData;
import server.ResponseException;
import server.ServerFacade;
import static ui.EscapeSequences.*;

public class PreLoginClient{
    private final ServerFacade server;

    public PreLoginClient(ServerFacade server) {
        this.server = server;
    }

    public String eval(String input) {
        var inputs = input.toLowerCase().split(" ");
        var command = (inputs.length > 0) ? inputs[0] : "help";

        return switch (command) {
            case "quit" -> "quit";
            case "login" -> "login";
            case "register" -> "register";
            default -> help();
        };
    }

    public String help() {
        String message = SET_TEXT_COLOR_BLUE + "\tquit " + SET_TEXT_COLOR_LIGHT_GREY + "- exit program\n";
        message = message + SET_TEXT_COLOR_BLUE + "\tregister " + SET_TEXT_COLOR_LIGHT_GREY + "- create and account\n";
        message = message + SET_TEXT_COLOR_BLUE + "\tlogin " + SET_TEXT_COLOR_LIGHT_GREY + "- an existing user\n";
        message = message + SET_TEXT_COLOR_BLUE + "\thelp " + SET_TEXT_COLOR_LIGHT_GREY + "- display this message again\n";
        return message;
    }

    public AuthData login(String username, String password) throws ResponseException {
        return server.login(new UserData(username, password, null));
    }

    public AuthData register(String username, String password, String email) throws ResponseException {
        return server.register(new UserData(username, password, email));
    }
}
