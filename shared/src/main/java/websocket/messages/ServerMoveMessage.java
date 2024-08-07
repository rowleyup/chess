package websocket.messages;

public class ServerMoveMessage extends ServerMessage {
    private final String message;

    public ServerMoveMessage(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }

    public String getMessage() { return message; }
}
