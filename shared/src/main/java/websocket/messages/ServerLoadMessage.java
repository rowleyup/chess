package websocket.messages;

public class ServerLoadMessage extends ServerMessage {
    private final chess.ChessGame game;
    private final boolean isCheck;
    private final boolean isCheckMate;

    public ServerLoadMessage(chess.ChessGame board, boolean isCheck, boolean isCheckMate) {
        super(ServerMessageType.LOAD_GAME);
        this.game = board;
        this.isCheck = isCheck;
        this.isCheckMate = isCheckMate;
    }

    public chess.ChessGame getGame() { return game; }

    public boolean isCheck() { return isCheck; }

    public boolean isCheckMate() { return isCheckMate; }
}
