package ui;

import model.AuthData;
import server.ServerFacade;

public class InGameClient {
    private AuthData userAuth;
    private final ServerFacade server;
    private final int gameId;
    private final String team;

    public InGameClient(String url, int id, String color) {
        server = new ServerFacade(url);
        gameId = id;
        team = color;
    }

    public void setAuth(AuthData auth) {
        userAuth = auth;
    }
}
