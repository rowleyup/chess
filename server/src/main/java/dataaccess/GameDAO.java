package dataaccess;

import model.GameData;
import server.handlers.ResponseException;

import java.util.Collection;

public interface GameDAO {
    Collection<GameData> getGames() throws DataAccessException;
    GameData getGameName(String gameName) throws DataAccessException;
    GameData getGameID(int gameID) throws DataAccessException;
    GameData createGame(String gameName) throws DataAccessException;
    String getColor(int gameID, String color) throws DataAccessException, ResponseException;
    boolean addPlayer(int gameID, String color, String username) throws DataAccessException, ResponseException;
    boolean clearGames() throws DataAccessException;
    boolean removeGame(int gameID) throws DataAccessException;
}
