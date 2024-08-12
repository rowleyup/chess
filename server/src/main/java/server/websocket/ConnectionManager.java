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
        Connection user = findUser(authToken, gameId);

        gameUserMap.forEach((game, list) -> {
            if (game.equals(Integer.toString(gameId))) {
                list.removeIf(c -> c.username.equals(user.username));
            }
        });
        if (user.role == Connection.SessionRole.WHITE) {
            GameData oldGame = gameDataMap.get(Integer.toString(gameId));
            GameData newGame = new GameData(oldGame.gameID(), null, oldGame.blackUsername(), oldGame.gameName(), oldGame.game());
            gameDataMap.put(Integer.toString(gameId), newGame);
            gameService.removePlayer(oldGame.gameID(), user.username, "white");
        }
        else if (user.role == Connection.SessionRole.BLACK) {
            GameData oldGame = gameDataMap.get(Integer.toString(gameId));
            GameData newGame = new GameData(oldGame.gameID(), oldGame.whiteUsername(), null, oldGame.gameName(), oldGame.game());
            gameDataMap.put(Integer.toString(gameId), newGame);
            gameService.removePlayer(oldGame.gameID(), user.username, "black");
        }

        if (gameUserMap.get(Integer.toString(gameId)).isEmpty()) {
            gameDataMap.remove(Integer.toString(gameId));
            gameUserMap.remove(Integer.toString(gameId));
            gameService.removeGame(gameId);
        }
        return user.username;
    }

    public synchronized void move(String authToken, int gameId, chess.ChessMove move) throws Exception {
        String checkmate = null;
        Connection user = findUser(authToken, gameId);
        if (user.isOver) {
            throw new ResponseException("Game is over");
        }
        if (user.role == Connection.SessionRole.OBSERVER) {
            throw new ResponseException("Observing cannot move pieces");
        }

        chess.ChessGame game = gameDataMap.get(Integer.toString(gameId)).game();
        chess.ChessPiece piece = game.getBoard().getPiece(move.getStartPosition());
        if (piece.getTeamColor() == chess.ChessGame.TeamColor.WHITE && user.role == Connection.SessionRole.BLACK) {
            throw new ResponseException("Cannot move the other team's piece");
        }
        else if (piece.getTeamColor() == chess.ChessGame.TeamColor.BLACK && user.role == Connection.SessionRole.WHITE) {
            throw new ResponseException("Cannot move the other team's piece");
        }
        else if (user.role == Connection.SessionRole.OBSERVER) {
            throw new ResponseException("Cannot move pieces as an observer");
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
                message = new ServerLoadMessage(game, true, false, "BLACK is in check");
            }
        }
        else if (user.role == Connection.SessionRole.BLACK) {
            if (game.isInCheckmate(chess.ChessGame.TeamColor.WHITE)) {
                message = new ServerLoadMessage(game, true, true, null);
                checkmate = "WHITE";
            }
            else if (game.isInCheck(chess.ChessGame.TeamColor.WHITE)) {
                message = new ServerLoadMessage(game, true, false, "WHITE is in check");
            }
        }

        broadcast(user.username, gameId, new ServerNotifyMessage(notification), null);
        broadcast(user.username, gameId, message, user.session);

        if (checkmate != null) {
            var m = new ServerNotifyMessage(String.format("GAME OVER: team %s is in checkmate", checkmate));
            broadcast(null, gameId, m, null);
            clearGame(gameId);
        }
        if (game.isInStalemate(chess.ChessGame.TeamColor.BLACK) || game.isInStalemate(chess.ChessGame.TeamColor.WHITE)) {
            var m = new ServerNotifyMessage("GAME OVER: stalemate");
            broadcast(null, gameId, m, null);
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

        switch (y) {
            case 1 -> spot = "a";
            case 2 -> spot = "b";
            case 3 -> spot = "c";
            case 4 -> spot = "d";
            case 5 -> spot = "e";
            case 6 -> spot = "f";
            case 7 -> spot = "g";
            case 8 -> spot = "h";
        }
        spot = spot + x;
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
