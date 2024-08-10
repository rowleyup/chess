package server.websocket;

import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import server.JsonUsage;
import server.ResponseException;
import service.GameService;
import websocket.messages.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public ConcurrentHashMap<String, ArrayList<Connection>> gameUserMap;
    public ConcurrentHashMap<String, GameData> gameDataMap;
    private final GameService gameService;

    public ConnectionManager(GameService gameService) {
        this.gameService = gameService;
        gameDataMap = new ConcurrentHashMap<>();
        gameUserMap = new ConcurrentHashMap<>();
    }

    public synchronized void join(String authToken, String username, int gameId, String role, Session session) {
        //Does not need to add player in gameService
        //Check if any new games have been created since
    }

    public synchronized void observe(String authToken, String username, int gameId, Session session) {
        //Check if any new games have been created
    }

    public synchronized void resign() {
        //Changes connection isOver to true
        gameService.clearPlayers();
    }

    public synchronized void leave(String username, int gameId) throws ResponseException {
        Connection user = null;
        for (Connection c : gameUserMap.get(Integer.toString(gameId))) {
            if (c.username.equals(username)) {
                user = c;
                break;
            }
        }
        if (user == null) {
            throw new ResponseException("Unable to find user");
        }

        if (user.role == Connection.SessionRole.WHITE && !user.isOver) {
            throw new ResponseException("Cannot leave while a player in an active game");
        }
        else if (user.role == Connection.SessionRole.BLACK && !user.isOver) {
            throw new ResponseException("Cannot leave while a player in an active game");
        }

        gameUserMap.forEach((game, list) -> {
            if (game.equals(Integer.toString(gameId))) {
                list.removeIf(c -> c.username.equals(username));
            }
        });
    }

    public synchronized void move() {
        //Changes connection isOver to true if checkmate
        //gameService.clearPlayers(); if checkmate
    }

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
