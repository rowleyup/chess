package websocket.messages;

public class ServerPlayerMessage extends ServerMessage {
    private final String message;

    public ServerPlayerMessage(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }

    public String getMessage() { return message; }
}
