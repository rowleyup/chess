package websocket.commands;

public class UserLeaveCommand extends UserGameCommand {
    public UserLeaveCommand(String authToken, Integer gameID) {
        super(CommandType.LEAVE, authToken, gameID);
    }
}
