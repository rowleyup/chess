package service;

import dataaccess.*;
import model.*;
import server.ResponseException;
import java.util.Collection;

/**
 * Contains game-related functions called by handlers
 */
public class GameService {
    private final GameDAO gameDao;
    private final AuthDAO authDao;

    public GameService(GameDAO gameDao, AuthDAO authDao) {
        this.gameDao = gameDao;
        this.authDao = authDao;
    }

    /**
     * Calls getGames from GameDAO, returns a list of active games
     *
     * @param authToken is a string containing an auth token
     * @return a Collection of GameData for all active games
     * @throws DataAccessException if null list was returned by GameDAO
     * @throws ResponseException if authentication failed
     */
    public Collection<GameData> listGames(String authToken) throws DataAccessException, ResponseException {
        authenticate(authToken);

        Collection<GameData> games = gameDao.getGames();
        if (games == null) {
            throw new DataAccessException("No games list");
        }

        return games;
    }

    /**
     * Calls createGame from GameDAO, returns ID of created game
     *
     * @param authToken is a string containing an auth token
     * @param gameName is a string containing the name of the game to be created
     * @return an int representing the created game's ID, or -1 if createGame returned null
     * @throws DataAccessException if thrown by createGame
     * @throws ResponseException if authentication failed
     */
    public int createGame(String authToken, String gameName) throws DataAccessException, ResponseException {
        authenticate(authToken);

        GameData game = gameDao.createGame(gameName);
        if (game == null) {
            return -1;
        }

        return game.gameID();
    }

    /**
     * Checks that game exists, calls addPlayer from GameDAO
     *
     * @param authToken is a string containing an auth token
     * @param gameID is an int containing the ID of the game to join
     * @param playerColor is a chess.ChessGame.TeamColor representing the team to join
     * @return true if successful
     * @throws DataAccessException if game does not exist
     * @throws ResponseException if thrown by addPlayer
     */
    public boolean joinGame(String authToken, int gameID, chess.ChessGame.TeamColor playerColor) throws DataAccessException, ResponseException {
        authenticate(authToken);

        GameData game = gameDao.getGameByID(gameID);
        if (game == null) {
            throw new DataAccessException("Game not found");
        }

        return gameDao.addPlayer(game, playerColor, authDao.getAuth(authToken).username());
    }

    /**
     * Calls clearGames from GameDAO and checks for success
     *
     * @return true if successful
     * @throws DataAccessException if unsuccessful
     */
    public boolean clear() throws DataAccessException {
        boolean done = gameDao.clearGames();
        if (!done) {
            throw new DataAccessException("Error: unable to clear game data");
        }

        return true;
    }

    public String authenticate(String authToken) throws DataAccessException, ResponseException {
        AuthData auth = authDao.getAuth(authToken);
        if (auth == null) {
            throw new ResponseException("Error: Unauthorized");
        }
        return auth.username();
    }
}
