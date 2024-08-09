package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.JsonUsage;
import websocket.commands.*;
import websocket.messages.ServerNotifyMessage;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = JsonUsage.fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> {
                UserConnectCommand action = JsonUsage.fromJson(message, UserConnectCommand.class);
                connect(action, session);
            }
            case LEAVE -> {
                UserLeaveCommand action = JsonUsage.fromJson(message, UserLeaveCommand.class);
                leave(action);
            }
            case MAKE_MOVE -> {
                UserMoveCommand action = JsonUsage.fromJson(message, UserMoveCommand.class);
                move(action);
            }
            case RESIGN -> {
                UserResignCommand action = JsonUsage.fromJson(message, UserResignCommand.class);
                resign(action);
            }
            default -> throw new IllegalStateException("Unexpected value: " + command.getCommandType());
        }
    }

    private void connect(UserConnectCommand action, Session session) throws IOException {
        String message;
        switch (action.getRole()) {
            case OBSERVER -> {
                connections.observe(action.getAuthToken().authToken(), action.getAuthToken().username() ,action.getGameID(), session);
                message = String.format("%s has joined the game as an observer", action.getAuthToken().username());
            }
            case WHITE -> {
                connections.join(action.getAuthToken().authToken(), action.getAuthToken().username(), action.getGameID(), "w", session);
                message = String.format("%s has joined the game as player WHITE", action.getAuthToken().username());
            }
            case BLACK -> {
                connections.join(action.getAuthToken().authToken(), action.getAuthToken().username(), action.getGameID(), "b", session);
                message = String.format("%s has joined the game as player BLACK", action.getAuthToken().username());
            }
            default -> throw new IllegalStateException("Unexpected value: " + action.getRole());
        }

        var notification = new ServerNotifyMessage(message);
        connections.broadcast(action.getAuthToken().username(), action.getGameID(), notification);
    }

    private void leave(UserLeaveCommand action) throws IOException {
        connections.leave(action.getAuthToken().username(), action.getGameID());
        String message = String.format("%s has left the game", action.getAuthToken().username());
        var notification = new ServerNotifyMessage(message);
        connections.broadcast(action.getAuthToken().username(), action.getGameID(), notification);
    }

    private void resign(UserResignCommand action) {}

    private void move(UserMoveCommand action) {}
}
