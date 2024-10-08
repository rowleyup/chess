package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.JsonUsage;
import server.ResponseException;
import service.GameService;
import websocket.commands.*;
import websocket.messages.ServerErrorMessage;
import websocket.messages.ServerNotifyMessage;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections;

    public WebSocketHandler(GameService gameService) {
        this.connections = new ConnectionManager(gameService);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = JsonUsage.fromJson(message, UserGameCommand.class);
        int gameId = command.getGameID();
        try {
            connections.updateGames(command.getAuthToken());
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
            error(e.getMessage(), session);
        }
    }

    private void connect(UserConnectCommand action, Session session) throws Exception {
        String message;
        String username;
        String role = connections.getRole(action.getAuthToken(), action.getGameID());
        username = connections.join(action.getAuthToken() ,action.getGameID(), role, session);
        if (role.equals("WHITE") || role.equals("BLACK")) {
            message = String.format("%s has joined the game as player %s", username, role);
        }
        else {
            message = String.format("%s has joined the game as an observer", username);
        }

        var notification = new ServerNotifyMessage(message);
        connections.broadcast(username, action.getGameID(), notification, null);
    }

    private void leave(UserLeaveCommand action) throws Exception {
        String username = connections.leave(action.getAuthToken(), action.getGameID());
        String message = String.format("%s has left the game", username);
        var notification = new ServerNotifyMessage(message);
        connections.broadcast(username, action.getGameID(), notification, null);
    }

    private void resign(UserResignCommand action) throws Exception {
        Connection c = connections.findUser(action.getAuthToken(), action.getGameID());
        String username = c.username;
        if (c.role == Connection.SessionRole.OBSERVER) {
            throw new ResponseException("Observer cannot resign");
        }
        if (c.isOver) {
            throw new ResponseException("Game is over");
        }

        String message = String.format("GAME OVER: %s has resigned", username);
        var notification = new ServerNotifyMessage(message);
        connections.broadcast(null, action.getGameID(), notification, null);
        connections.clearGame(action.getGameID());
    }

    private void move(UserMoveCommand action) throws Exception {
        connections.move(action.getAuthToken(), action.getGameID(), action.getMove());
    }

    private void error(String message, Session session) throws Exception {
        connections.broadcast(null, -1, new ServerErrorMessage(message), session);
    }
}
