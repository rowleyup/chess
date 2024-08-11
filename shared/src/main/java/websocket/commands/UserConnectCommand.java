package websocket.commands;

public class UserConnectCommand extends UserGameCommand{
    private final UserRole role;

    public UserConnectCommand(String authToken, Integer gameID, UserRole role) {
        super(CommandType.CONNECT, authToken, gameID);
        this.role = role;
    }

    public enum UserRole {
        WHITE,
        BLACK,
        OBSERVER
    }

    public UserRole getRole() { return role; }
}
