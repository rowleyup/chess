package dataaccess;

import chess.ChessGame;
import model.GameData;
import server.handlers.ResponseException;

import java.util.Collection;
import java.util.List;

public class MySqlGameDAO implements GameDAO {
    public MySqlGameDAO() throws DataAccessException {
        TableCreator.configureDatabase("game");
        TableCreator.configureDatabase("gameUsers");
    }

    public Collection<GameData> getGames() throws DataAccessException {
        return List.of();
    }

    public GameData getGameByName(String gameName) throws DataAccessException {
        return null;
    }

    public GameData getGameByID(int gameID) throws DataAccessException {
        return null;
    }

    public GameData createGame(String gameName) throws DataAccessException {
        return null;
    }

    public boolean addPlayer(GameData game, ChessGame.TeamColor color, String username) throws DataAccessException, ResponseException {
        return false;
    }

    public boolean clearGames() throws DataAccessException {
        return false;
    }

    public boolean updateGame(GameData game) throws DataAccessException {
        return false;
    }
}
