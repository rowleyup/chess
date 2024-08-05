package ui;

import model.AuthData;
import server.ResponseException;
import server.ServerFacade;

import java.util.HashMap;

import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;
import static ui.EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY;

public class PostLoginClient {
    private final ServerFacade server;
    private final InGameClient nextClient;
    private AuthData userAuth;
    private HashMap<String, String[]> gameList;

    public PostLoginClient(String url, InGameClient client) {
        server = new ServerFacade(url);
        nextClient = client;
        gameList = new HashMap<>();
    }

    public String help() {
        String message = SET_TEXT_COLOR_BLUE + "\tlogout " + SET_TEXT_COLOR_LIGHT_GREY + "- logout user\n";
        message = message + SET_TEXT_COLOR_BLUE + "\tcreate " + SET_TEXT_COLOR_LIGHT_GREY + "- create a chess game\n";
        message = message + SET_TEXT_COLOR_BLUE + "\tlist " + SET_TEXT_COLOR_LIGHT_GREY + "- list all existing chess games\n";
        message = message + SET_TEXT_COLOR_BLUE + "\tplay " + SET_TEXT_COLOR_LIGHT_GREY + "- join a chess game as a player\n";
        message = message + SET_TEXT_COLOR_BLUE + "\tobserve " + SET_TEXT_COLOR_LIGHT_GREY + "- join a chess game as an observer\n";
        message = message + SET_TEXT_COLOR_BLUE + "\thelp " + SET_TEXT_COLOR_LIGHT_GREY + "- display this message again\n";
        return message;
    }

    public String logout() throws ResponseException {
        if (userAuth == null) {
            throw new ResponseException("Authentication data not found");
        }
        server.logout(userAuth.authToken());

        return "Logged out successfully";
    }

    public String list() {}

    public void create(String name) {}

    public int observe(int id) {}

    public int join(int id, String color) {}

    public void setAuth(AuthData auth) {
        userAuth = auth;
    }

    private void updateList() {}
}
