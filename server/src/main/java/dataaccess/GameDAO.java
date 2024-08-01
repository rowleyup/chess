package dataaccess;

import model.GameData;
import server.ResponseException;

import java.util.Collection;

public interface GameDAO {

    /**
     * Returns a list of active games
     *
     * @return a Collection containing a GameData object for each active game
     * @throws DataAccessException if unable to read from the database
     */
    Collection<GameData> getGames() throws DataAccessException;

    /**
     * Returns the GameData object for the game with the given name
     *
     * @param gameName is a string representing the game name
     * @return the GameData object for the requested game, or null if it does not exist
     * @throws DataAccessException if unable to read from the database
     */
    GameData getGameByName(String gameName) throws DataAccessException;

    /**
     * Returns the GameData object for the game with the given ID
     *
     * @param gameID is an int representing the game ID
     * @return the GameData object for the requested game, or null if it does not exist
     * @throws DataAccessException if unable to read from the database
     */
    GameData getGameByID(int gameID) throws DataAccessException;

    /**
     * Creates a new game with the given name
     *
     * @param gameName is a string representing the game name
     * @return the GameData object for the created game, or null if the name is taken
     * @throws DataAccessException if unable to update database
     */
    GameData createGame(String gameName) throws DataAccessException;

    /**
     * Adds a player to an already existing game
     *
     * @param game is a GameData object representing the desired game
     * @param color is the chess.ChessGame.TeamColor option the player will play as (WHITE/BLACK)
     * @param username is the username of the player to add
     * @return true if successful, false if the color is already taken
     * @throws DataAccessException if unable to update database
     * @throws ResponseException if invalid color
     */
    boolean addPlayer(GameData game, chess.ChessGame.TeamColor color, String username) throws DataAccessException, ResponseException;

    /**
     * Clears all game data from the database
     *
     * @return true if successful
     * @throws DataAccessException if unable to update database
     */
    boolean clearGames() throws DataAccessException;

    /**
     * Updates the fields of a game
     *
     * @param game is the GameData object of the active game to be updated
     * @return true if successful
     * @throws DataAccessException if unable to update database
     */
    boolean updateGame(GameData game) throws DataAccessException;
}
