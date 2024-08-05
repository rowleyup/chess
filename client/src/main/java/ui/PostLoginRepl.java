package ui;

import java.util.Scanner;

public class PostLoginRepl {
    private final PostLoginClient client;
    private final InGameRepl inGame;
    private final Scanner scanner;

    public PostLoginRepl(String url) {
        inGame = new InGameRepl(url);
        client = new PostLoginClient(url, inGame.client());
        scanner = new Scanner(System.in);
    }

    public void run() {

    }

    public void printPrompt() {

    }

    public PostLoginClient client() {
        return client;
    }
}
