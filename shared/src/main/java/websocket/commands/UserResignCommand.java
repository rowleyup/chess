package websocket.commands;

import model.AuthData;

public class UserResignCommand extends UserGameCommand {
    public UserResignCommand(AuthData authToken, Integer gameID) {
        super(CommandType.RESIGN, authToken, gameID);
    }
}
