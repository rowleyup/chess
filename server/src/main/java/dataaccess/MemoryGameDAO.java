package dataaccess;

import chess.ChessGame;
import model.GameData;
import server.handlers.ResponseException;
import java.util.Collection;
import java.util.HashSet;

/**
 * Implements GameDAO and stores data in memory
 */
public class MemoryGameDAO implements GameDAO {
    private final HashSet<GameData> games;
    private int nextID;

    public MemoryGameDAO() {
        games = new HashSet<>();
        nextID = 1111;
    }

    public Collection<GameData> getGames() {
        return games;
    }

    public GameData getGameByName(String gameName) {
        GameData result = null;
        for (GameData game : games) {
            if (gameName.equals(game.gameName())) {
                result = game;
                break;
            }
        }
        return result;
    }

    public GameData getGameByID(int gameID) {
        GameData result = null;
        for (GameData game : games) {
            if (gameID == game.gameID()) {
                result = game;
                break;
            }
        }
        return result;
    }

    public GameData createGame(String gameName) {
        GameData old = getGameByName(gameName);
        if (old != null) {
            return null;
        }

        GameData game = new GameData(nextID, null, null, gameName, new ChessGame());
        nextID = nextID + 1111;
        games.add(game);
        return game;
    }

    public boolean addPlayer(GameData gameReq, chess.ChessGame.TeamColor color, String username) throws ResponseException {
        GameData result;

        GameData game = getGameByID(gameReq.gameID());
        if (game == null) {
            throw new ResponseException("Game not found");
        }

        if (color == chess.ChessGame.TeamColor.WHITE) {
            if (game.whiteUsername() != null) {
                return false;
            }
            result = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
            return updateGame(result);
        }
        else if (color == chess.ChessGame.TeamColor.BLACK) {
            if (game.blackUsername() != null) {
                return false;
            }
            result = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
            return updateGame(result);
        }
        else {
            throw new ResponseException("Error: invalid color");
        }
    }

    public boolean clearGames() {
        games.clear();
        nextID = 1111;
        return true;
    }

    public boolean updateGame(GameData game) {
        int id = game.gameID();
        GameData old = getGameByID(id);
        if (old == null) {
            return false;
        }
        games.remove(old);
        games.add(game);
        return true;
    }
}
