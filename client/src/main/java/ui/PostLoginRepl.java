package ui;

import java.util.Scanner;

public class PostLoginRepl implements Repl{
    private final PostLoginClient client;
    private final Repl inGame;
    private final Scanner scanner;

    public PostLoginRepl(String url) {
        inGame = new InGameRepl(url);
        client = new PostLoginClient(url, inGame.client());
        scanner = new Scanner(System.in);
    }

    @Override
    public void run() {

    }

    @Override
    public void printPrompt() {

    }

    public PostLoginClient client() {
        return client;
    }
}
