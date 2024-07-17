package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.List;

public class MemoryGameDAO implements GameDAO {
    public Collection<ChessGame> getGames() throws DataAccessException {
        return List.of();
    }

    public GameData getGameName(String gameName) throws DataAccessException {
        return null;
    }

    public GameData getGameID(String gameID) throws DataAccessException {
        return null;
    }

    public GameData createGame(String gameName) throws DataAccessException {
        return null;
    }

    public String getColor(GameData game, String color) throws DataAccessException {
        return "";
    }

    public boolean addPlayer(GameData game, String color, String authToken) throws DataAccessException {
        return false;
    }

    public boolean clearGames() throws DataAccessException {
        return false;
    }
}
