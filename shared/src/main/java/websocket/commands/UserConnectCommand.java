package websocket.commands;

public class UserConnectCommand extends UserGameCommand{
    public UserConnectCommand(String authToken, Integer gameID) {
        super(CommandType.CONNECT, authToken, gameID);
    }
}
