package dataaccess;

import chess.ChessGame;
import model.GameData;
import server.handlers.ResponseException;

import java.util.Collection;
import java.util.HashSet;

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

    public GameData createGame(String gameName) throws DataAccessException {
        GameData old = getGameByName(gameName);
        if (old != null) {
            throw new DataAccessException("Error: game name already exists");
        }

        GameData game = new GameData(nextID, null, null, gameName, new ChessGame());
        nextID = nextID + 1111;
        games.add(game);
        return game;
    }

    public boolean addPlayer(int gameID, String color, String username) throws ResponseException {
        GameData result = getGameByID(gameID);
        GameData result2;

        if (color.equals("WHITE")) {
            if (result.whiteUsername() != null) {
                throw new ResponseException("Error: color already taken");
            }
            result2 = new GameData(result.gameID(), username, result.blackUsername(), result.gameName(), result.game());
            return updateGame(result2);
        }
        else if (color.equals("BLACK")) {
            if (result.blackUsername() != null) {
                throw new ResponseException("Error: color already taken");
            }
            result2 = new GameData(result.gameID(), result.whiteUsername(), username, result.gameName(), result.game());
            return updateGame(result2);
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

    public boolean removeGame(int gameID) {
        GameData rem = getGameByID(gameID);
        games.remove(rem);
        return true;
    }

    public boolean updateGame(GameData game) {
        int id = game.gameID();
        GameData old = getGameByID(id);
        if (old == null) {
            games.add(game);
            return true;
        }
        games.remove(old);
        games.add(game);
        return true;
    }
}
