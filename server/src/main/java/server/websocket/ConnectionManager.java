package server.websocket;

import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.*;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public ConcurrentHashMap<String, ArrayList<Connection>> gameUserMap = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String, GameData> gameDataMap = new ConcurrentHashMap<>();

    public synchronized void join(String authToken, String username, int gameId, String role, Session session) {}

    public synchronized void observe(String authToken, String username, int gameId, Session session) {}

    public synchronized void leave(String username, int gameId) {}

    public synchronized void move() {}

    public synchronized void broadcast(String username, int gameId, ServerMessage message) {}
}
