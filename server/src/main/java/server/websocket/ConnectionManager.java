package server.websocket;

import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import server.JsonUsage;
import server.ResponseException;
import service.GameService;
import websocket.messages.*;
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

    public synchronized String join(String authToken, int gameId, String role, Session session) throws Exception {
        String username = gameService.authenticate(authToken);
        var list = gameUserMap.get(Integer.toString(gameId));
        var roleEnum = Connection.SessionRole.valueOf(role);
        list.add(new Connection(username, session, roleEnum));

        var board = gameDataMap.get(Integer.toString(gameId)).game();
        boolean isCheck = false;
        boolean isCheckmate = false;
        if (role.equals("WHITE") || role.equals("BLACK")) {
            isCheck = board.isInCheck(chess.ChessGame.TeamColor.valueOf(role));
            isCheckmate = board.isInCheckmate(chess.ChessGame.TeamColor.valueOf(role));
        }
        var message = new ServerLoadMessage(board, isCheck, isCheckmate, null);
        broadcast(null, -1, message, session);
        return username;
    }

    public synchronized String getRole(String authToken, int gameId) throws Exception {
        String username = gameService.authenticate(authToken);
        String role = gameService.getRole(username, gameId);
        if (role == null) {
            return "OBSERVER";
        }
        return role;
    }

    public synchronized void clearGame(int gameId) {
        for (Connection c : gameUserMap.get(Integer.toString(gameId))) {
            c.isOver = true;
        }
    }

    public synchronized String leave(String authToken, int gameId) throws Exception {
        String username = gameService.authenticate(authToken);
        Connection user = findUser(username, gameId);
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

        if (gameUserMap.get(Integer.toString(gameId)).isEmpty()) {
            gameDataMap.remove(Integer.toString(gameId));
            gameUserMap.remove(Integer.toString(gameId));
            gameService.removeGame(gameId);
        }
        return username;
    }

    public synchronized void move(String authToken, int gameId, chess.ChessMove move) throws Exception {
        String checkmate = null;
        Connection user = findUser(authToken, gameId);
        chess.ChessGame game = gameDataMap.get(Integer.toString(gameId)).game();
        chess.ChessPiece piece = game.getBoard().getPiece(move.getStartPosition());
        if (!(piece.getTeamColor() == chess.ChessGame.TeamColor.WHITE && user.role == Connection.SessionRole.WHITE)) {
            throw new ResponseException("Cannot move the other team's piece");
        }
        else if (!(piece.getTeamColor() == chess.ChessGame.TeamColor.BLACK && user.role == Connection.SessionRole.BLACK)) {
            throw new ResponseException("Cannot move the other team's piece");
        }

        game.makeMove(move);

        String start = convertToCoordinates(move.getStartPosition());
        String end = convertToCoordinates(move.getEndPosition());
        String notification = String.format("%s moved piece from %s to %s", user.username, start, end);
        var message = new ServerLoadMessage(game, false, false, null);

        if (user.role == Connection.SessionRole.WHITE) {
            if (game.isInCheckmate(chess.ChessGame.TeamColor.BLACK)) {
                message = new ServerLoadMessage(game, true, true, null);
                checkmate = "BLACK";
            }
            else if (game.isInCheck(chess.ChessGame.TeamColor.BLACK)) {
                message = new ServerLoadMessage(game, true, false, null);
            }
        }
        else if (user.role == Connection.SessionRole.BLACK) {
            if (game.isInCheckmate(chess.ChessGame.TeamColor.WHITE)) {
                message = new ServerLoadMessage(game, true, true, null);
                checkmate = "WHITE";
            }
            else if (game.isInCheck(chess.ChessGame.TeamColor.WHITE)) {
                message = new ServerLoadMessage(game, true, false, null);
            }
        }

        broadcast(user.username, gameId, message, null);
        broadcast(user.username, gameId, new ServerNotifyMessage(notification), null);
        var message2 = new ServerLoadMessage(message.getGame(), message.isCheck(), message.isCheckMate(), null);
        broadcast(null, -1, message2, user.session);

        if (checkmate != null) {
            var m = new ServerNotifyMessage(String.format("GAME OVER: team %s is in checkmate", checkmate));
            broadcast(null, gameId, m, null);
            clearGame(gameId);
        }
    }

    public synchronized void broadcast(String excludeUsername, int gameId, ServerMessage message, Session session) throws Exception {
        var list = gameUserMap.get(Integer.toString(gameId));
        if (list != null) {
            for (Connection conn : list) {
                if (!conn.username.equals(excludeUsername)) {
                    if (session != conn.session) {
                        if (conn.session.isOpen()) {
                            conn.send(JsonUsage.getJson(message));
                        }
                    }
                }
            }
        }
        if (session != null) {
            if (session.isOpen()) {
                session.getRemote().sendString(JsonUsage.getJson(message));
            }
        }
    }

    public synchronized Connection findUser(String authToken, int gameId) throws Exception {
        Connection user = null;
        String username = gameService.authenticate(authToken);
        for (Connection c : gameUserMap.get(Integer.toString(gameId))) {
            if (c.username.equals(username)) {
                user = c;
                break;
            }
        }
        if (user == null) {
            throw new ResponseException("Unable to find user");
        }
        return user;
    }

    private String convertToCoordinates(chess.ChessPosition pos) {
        int x = pos.getRow();
        int y = pos.getColumn();
        String spot = "";

        switch (x) {
            case 1 -> spot = "a";
            case 2 -> spot = "b";
            case 3 -> spot = "c";
            case 4 -> spot = "d";
            case 5 -> spot = "e";
            case 6 -> spot = "f";
            case 7 -> spot = "g";
            case 8 -> spot = "h";
        }
        spot = spot + y;
        return spot;
    }

    public synchronized void updateGames(String authToken) throws Exception {
        var games = gameService.listGames(authToken);
        for (GameData game : games) {
            String id = Integer.toString(game.gameID());
            if (!gameDataMap.containsKey(id)) {
                gameDataMap.put(id, game);
                gameUserMap.put(id, new ArrayList<>());
            }
        }
    }
}
