package websocket.commands;

public class UserResignCommand extends UserGameCommand {
    public UserResignCommand(String authToken, Integer gameID) {
        super(CommandType.RESIGN, authToken, gameID);
    }
}
