package ui;

public abstract class ChessClient {
    private final String serverUrl;
    public ChessClient(String serverUrl) {
        this.serverUrl = serverUrl;
    }
    abstract String eval(String input);
    abstract String help();
}
