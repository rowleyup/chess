package websocket.messages;

public class ServerMoveMessage extends ServerMessage {
    private final String message;
    private final boolean isCheck;
    private final boolean isCheckMate;

    public ServerMoveMessage(String message, boolean isCheck, boolean isCheckMate) {
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
        this.isCheck = isCheck;
        this.isCheckMate = isCheckMate;
    }

    public String getMessage() { return message; }

    public boolean isCheck() { return isCheck; }

    public boolean isCheckMate() { return isCheckMate; }
}
