package websocket.messages;

public class ServerLoadMessage extends ServerMessage {
    private final chess.ChessBoard game;

    public ServerLoadMessage(chess.ChessBoard board) {
        super(ServerMessageType.LOAD_GAME);
        this.game = board;
    }

    public chess.ChessBoard getGame() { return game; }
}
