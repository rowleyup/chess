package websocket.commands;

import model.AuthData;

public class UserLeaveCommand extends UserGameCommand {
    public UserLeaveCommand(AuthData authToken, Integer gameID) {
        super(CommandType.LEAVE, authToken, gameID);
    }
}
