package server.websocket;

import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import server.JsonUsage;
import websocket.messages.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public ConcurrentHashMap<String, ArrayList<Connection>> gameUserMap = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String, GameData> gameDataMap = new ConcurrentHashMap<>();

    public synchronized void join(String authToken, String username, int gameId, String role, Session session) {}

    public synchronized void observe(String authToken, String username, int gameId, Session session) {}

    public synchronized void leave(String username, int gameId) {
        Connection user = null;
        for (Connection c : gameUserMap.get(Integer.toString(gameId))) {
            if (c.username.equals(username)) {
                user = c;
                break;
            }
        }

        if (user == null) {
            throw new RuntimeException("");
        }

        gameUserMap.get(Integer.toString(gameId)).remove(user);
        if (user.role == Connection.SessionRole.WHITE) {
            //TODO: figure out how to change things in map and in database
        }
    }

    public synchronized void move() {}

    public synchronized void broadcast(String excludeUsername, int gameId, ServerMessage message) throws IOException {
        for (Connection conn : gameUserMap.get(Integer.toString(gameId))) {
            if (!conn.username.equals(excludeUsername)) {
                if (conn.session.isOpen()) {
                    conn.send(JsonUsage.getJson(message));
                }
            }
        }
    }
}
