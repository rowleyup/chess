package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.JsonUsage;
import service.GameService;
import websocket.commands.*;
import websocket.messages.ServerErrorMessage;
import websocket.messages.ServerNotifyMessage;
import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections;

    public WebSocketHandler(GameService gameService) {
        this.connections = new ConnectionManager(gameService);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = JsonUsage.fromJson(message, UserGameCommand.class);
        connections.updateGames(command.getAuthToken());
        int gameId = command.getGameID();
        try {
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
        } catch (Throwable e) {
            error(e.getMessage(), gameId);
        }
    }

    private void connect(UserConnectCommand action, Session session) throws Exception {
        String message;
        String username;
        String role = connections.getRole(action.getAuthToken(), action.getGameID());
        username = connections.join(action.getAuthToken() ,action.getGameID(), role, session);
        message = String.format("%s has joined the game as an observer", username);

        var notification = new ServerNotifyMessage(message);
        connections.broadcast(username, action.getGameID(), notification);
    }

    private void leave(UserLeaveCommand action) throws Exception {
        String username = connections.leave(action.getAuthToken(), action.getGameID());
        String message = String.format("%s has left the game", username);
        var notification = new ServerNotifyMessage(message);
        connections.broadcast(username, action.getGameID(), notification);
    }

    private void resign(UserResignCommand action) throws Exception {
        String username = connections.findUser(action.getAuthToken(), action.getGameID()).username;
        connections.clearGame(action.getGameID());
        String message = String.format("GAME OVER: %s has resigned", username);
        var notification = new ServerNotifyMessage(message);
        connections.broadcast(username, action.getGameID(), notification);
    }

    private void move(UserMoveCommand action) throws Exception {
        String username = connections.findUser(action.getAuthToken(), action.getGameID()).username;
        connections.move(username, action.getGameID(), action.getMove());
    }

    private void error(String message, int gameId) throws IOException {
        connections.broadcast(null, gameId, new ServerErrorMessage(message));
    }
}
