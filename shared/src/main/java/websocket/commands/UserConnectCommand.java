package websocket.commands;

import model.AuthData;

public class UserConnectCommand extends UserGameCommand{
    private final UserRole role;

    public UserConnectCommand(AuthData authToken, Integer gameID, UserRole role) {
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
