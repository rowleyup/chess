package ui;

import model.AuthData;
import server.ServerFacade;

import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;
import static ui.EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY;

public class InGameClient {
    private AuthData userAuth;
    private final ServerFacade server;
    private int gameId;
    private String team;

    public InGameClient(ServerFacade server, AuthData auth) {
        this.server = server;
        this.userAuth = auth;
    }

    public void setId(int id) {
        gameId = id;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String help() {
        String message = SET_TEXT_COLOR_BLUE + "\n\tquit " + SET_TEXT_COLOR_LIGHT_GREY + "- leave game\n";
        message = message + SET_TEXT_COLOR_BLUE + "\twhite " + SET_TEXT_COLOR_LIGHT_GREY + "- print chess board from white perspective\n";
        message = message + SET_TEXT_COLOR_BLUE + "\tblack " + SET_TEXT_COLOR_LIGHT_GREY + "- print chess board from black perspective\n";
        message = message + SET_TEXT_COLOR_BLUE + "\thelp " + SET_TEXT_COLOR_LIGHT_GREY + "- display this message again\n";
        return message;
    }
}
