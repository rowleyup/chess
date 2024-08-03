package ui;

import server.ResponseException;
import server.ServerFacade;
import java.util.Arrays;

public class PreLoginClient implements ChessClient{
    private final String serverUrl;
    private final ServerFacade server;

    public PreLoginClient(String serverUrl) {
        this.serverUrl = serverUrl;
        server = new ServerFacade(serverUrl);
    }

    @Override
    public String eval(String input) {
        try {
            var inputs = input.toLowerCase().split(" ");
            var command = (inputs.length > 0) ? inputs[0] : "help";

            return switch (command) {
                case "quit" -> quit();
                case "login" -> login();
                case "register" -> register();
                default -> help();
            };
        } catch (ResponseException e) {
            return e.getMessage();
        }
    }

    @Override
    public String help() {
        return "";
    }

    public String quit() {
        return "";
    }

    public String login() {
        return "";
    }

    public String register() {
        return "";
    }
}
