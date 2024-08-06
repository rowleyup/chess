package ui;

import model.AuthData;
import model.GameData;
import request.*;
import server.ResponseException;
import server.ServerFacade;
import java.util.Collection;
import java.util.HashMap;
import static ui.EscapeSequences.*;

public class PostLoginClient {
    private final ServerFacade server;
    private final AuthData userAuth;
    private HashMap<String, GameData> gameList;

    public PostLoginClient(ServerFacade server, AuthData auth) {
        this.server = server;
        gameList = new HashMap<>();
        userAuth = auth;
    }

    public String help() {
        String message = SET_TEXT_COLOR_BLUE + "\n\tlogout " + SET_TEXT_COLOR_LIGHT_GREY + "- logout user\n";
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

    public String list() throws ResponseException {
        updateList();
        StringBuilder table = new StringBuilder(SET_TEXT_UNDERLINE + "  ID  NAME          PLAYER_WHITE  PLAYER_BLACK");
        table.append(RESET_TEXT_UNDERLINE + "\n");
        var keys = gameList.keySet();
        for (String key : keys) {
            table.append(tableTemplate(key, gameList.get(key)));
        }

        return table.toString();
    }

    public void create(String name) throws ResponseException {
        if (name == null || name.isEmpty()) {
            throw new ResponseException("Error: no game name given");
        }
        if (userAuth == null) {
            throw new ResponseException("Authentication data not found");
        }

        server.createGame(userAuth.authToken(), new GameData(0, null, null, name, null));
    }

    public GameData observe(int id) {
        return gameList.get(Integer.toString(id));
    }

    public GameData join(int id, String color) throws ResponseException {
        JoinRequest req;
        GameData game = gameList.get(Integer.toString(id));
        int gameId = game.gameID();
        if (color.equals("w") || color.equals("white")) {
            req = new JoinRequest(chess.ChessGame.TeamColor.WHITE, gameId);
        }
        else {
            req = new JoinRequest(chess.ChessGame.TeamColor.BLACK, gameId);
        }

        server.joinGame(userAuth.authToken(), req);
        return game;
    }

    private void updateList() throws ResponseException {
        gameList = new HashMap<>();
        ListResponse res = server.listGames(userAuth.authToken());
        Collection<GameData> games = res.games();
        int i = 1;
        for (GameData g: games) {
            gameList.put(Integer.toString(i), g);
            i++;
        }
    }

    private String tableTemplate(String id, GameData g) {
        String m = "  " + id + " ".repeat(Math.max(0, 4 - id.length())) + g.gameName();
        m = m + " ".repeat(Math.max(0, 14 - g.gameName().length()));

        if (g.whiteUsername() != null) {
            m = m + g.whiteUsername() + " ".repeat(Math.max(0, 14 - g.whiteUsername().length()));
        }
        else {
            m = m + "none" + " ".repeat(10);
        }

        if (g.blackUsername() != null) {
            m = m + g.blackUsername() + " ".repeat(Math.max(0, 12 - g.blackUsername().length())) + "\n";
        }
        else {
            m = m + "none" + " ".repeat(10);
        }
        m = m + "\n";

        return m;
    }
}
