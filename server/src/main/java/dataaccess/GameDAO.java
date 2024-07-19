package dataaccess;

import model.GameData;
import server.handlers.ResponseException;

import java.util.Collection;

public interface GameDAO {
    Collection<GameData> getGames() throws DataAccessException;
    GameData getGameByName(String gameName) throws DataAccessException;
    GameData getGameByID(int gameID) throws DataAccessException;
    GameData createGame(String gameName) throws DataAccessException;
    boolean addPlayer(GameData game, String color, String username) throws DataAccessException, ResponseException;
    boolean clearGames() throws DataAccessException;
    boolean removeGame(int gameID) throws DataAccessException;
    boolean updateGame(GameData game) throws DataAccessException;
}
