package ui;

import model.AuthData;
import model.UserData;
import server.ResponseException;
import server.ServerFacade;
import static ui.EscapeSequences.*;

public class PreLoginClient{
    private final ServerFacade server;
    private final PostLoginClient nextClient;

    public PreLoginClient(String serverUrl, PostLoginClient nextClient) {
        server = new ServerFacade(serverUrl);
        this.nextClient = nextClient;
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

    public void login(String username, String password) throws ResponseException {
        AuthData auth = server.login(new UserData(username, password, null));
        nextClient.setAuth(auth);
    }

    public void register(String username, String password, String email) throws ResponseException {
        AuthData auth = server.register(new UserData(username, password, email));
        nextClient.setAuth(auth);
    }
}
