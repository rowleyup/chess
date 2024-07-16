package dataaccess;

import chess.ChessGame;
import model.GameData;
import java.util.Collection;

public interface GameDAO {
    Collection<ChessGame> getGames() throws DataAccessException;
    GameData getGameName(String gameName) throws DataAccessException;
    GameData getGameID(String gameID) throws DataAccessException;
    GameData createGame(String gameName) throws DataAccessException;
    String getColor(GameData game, String color) throws DataAccessException;
    boolean addPlayer(GameData game, String color, String authToken) throws DataAccessException;
    boolean clearGames() throws DataAccessException;
}
