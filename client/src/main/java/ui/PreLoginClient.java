package ui;

public class PreLoginClient implements ChessClient{
    private final String serverUrl;

    public PreLoginClient(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    @Override
    public String eval(String input) {
        return "";
    }

    @Override
    public String help() {
        return "";
    }
}
