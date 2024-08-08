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
        String message = SET_TEXT_COLOR_BLUE + "\n\tredraw " + SET_TEXT_COLOR_LIGHT_GREY + "- redraw chess board\n";
        message = message + SET_TEXT_COLOR_BLUE + "\tmove " + SET_TEXT_COLOR_LIGHT_GREY + "- move a chess piece\n";
        message = message + SET_TEXT_COLOR_BLUE + "\thighlight " + SET_TEXT_COLOR_LIGHT_GREY + "- highlight legal moves for a piece\n";
        message = message + SET_TEXT_COLOR_BLUE + "\tresign " + SET_TEXT_COLOR_LIGHT_GREY + "- resign from the game\n";
        message = message + SET_TEXT_COLOR_BLUE + "\tleave " + SET_TEXT_COLOR_LIGHT_GREY + "- leave game\n";
        message = message + SET_TEXT_COLOR_BLUE + "\thelp " + SET_TEXT_COLOR_LIGHT_GREY + "- display this message again\n";
        return message;
    }

    public String overHelp() {
        String message = SET_TEXT_COLOR_BLUE + "\n\tredraw " + SET_TEXT_COLOR_LIGHT_GREY + "- redraw chess board\n";
        message = message + SET_TEXT_COLOR_LIGHT_GREY + "\tmove - move a chess piece\n";
        message = message + SET_TEXT_COLOR_LIGHT_GREY + "\thighlight - highlight legal moves for a piece\n";
        message = message + SET_TEXT_COLOR_LIGHT_GREY + "\tresign - resign from the game\n";
        message = message + SET_TEXT_COLOR_BLUE + "\tleave " + SET_TEXT_COLOR_LIGHT_GREY + "- leave game\n";
        message = message + SET_TEXT_COLOR_BLUE + "\thelp " + SET_TEXT_COLOR_LIGHT_GREY + "- display this message again\n";
        return message;
    }

    public String leave() {}

    public String move() {}
}
