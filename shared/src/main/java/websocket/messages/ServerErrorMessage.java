package websocket.messages;

public class ServerErrorMessage extends ServerMessage {
    private final String errorMessage;

    public ServerErrorMessage(String message) {
        super(ServerMessageType.ERROR);
        errorMessage = "Error: " + message;
    }

    public String getErrorMessage() { return errorMessage; }
}
