package ui;

import model.AuthData;
import server.ServerFacade;

public class InGameClient {
    private AuthData userAuth;
    private final ServerFacade server;
    private int gameId;
    private String team;

    public InGameClient(String url) {
        server = new ServerFacade(url);
    }

    public void setAuth(AuthData auth) {
        userAuth = auth;
    }
}
