package websocket.commands;

import model.AuthData;

public class UserMoveCommand extends UserGameCommand {
    private final chess.ChessMove move;

    public UserMoveCommand(AuthData authToken, Integer gameID, chess.ChessMove move) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.move = move;
    }

    public chess.ChessMove getMove() { return move; }
}
