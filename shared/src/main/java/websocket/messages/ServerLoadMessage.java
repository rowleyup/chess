package websocket.messages;

public class ServerLoadMessage extends ServerMessage {
    private final chess.ChessGame game;
    private final boolean isCheck;
    private final boolean isCheckMate;
    private final String message;

    public ServerLoadMessage(chess.ChessGame board, boolean isCheck, boolean isCheckMate, String message) {
        super(ServerMessageType.LOAD_GAME);
        this.game = board;
        this.isCheck = isCheck;
        this.isCheckMate = isCheckMate;
        this.message = message;
    }

    public chess.ChessGame getGame() { return game; }

    public boolean isCheck() { return isCheck; }

    public boolean isCheckMate() { return isCheckMate; }

    public String getMessage() { return message; }
}
