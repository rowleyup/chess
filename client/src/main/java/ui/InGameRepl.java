package ui;

import java.util.Scanner;

public class InGameRepl {
    private final InGameClient client;
    private final Scanner scanner;
    private int gameId;
    private String team;

    public InGameRepl(String url) {
        client = new InGameClient(url, gameId, team);
        scanner = new Scanner(System.in);
    }

    public InGameClient client() {
        return client;
    }

    public void run(int game, String color) {
        gameId = game;
        team = color;
    }
}
