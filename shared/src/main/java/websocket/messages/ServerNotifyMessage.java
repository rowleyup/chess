package websocket.messages;

public class ServerNotifyMessage extends ServerMessage {
    private final String message;

    public ServerNotifyMessage(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }

    public String getMessage() { return message; }
}
