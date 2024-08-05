package ui;

import server.ServerFacade;

public class PostLoginClient {
    private final ServerFacade server;
    private final InGameClient nextClient;

    public PostLoginClient(String url, InGameClient client) {
        server = new ServerFacade(url);
        nextClient = client;
    }

    public String help() {}

    public String logout() {}

    public String list() {}

    public void create(String name) {}

    public int observe(int id) {}

    public int join(int id, String color) {}
}
