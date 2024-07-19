package service;

import dataaccess.*;
import model.*;
import server.handlers.ResponseException;

import java.util.Collection;

public class GameService {
    private final GameDAO gameDao;
    private final AuthDAO authDao;

    public GameService(GameDAO gameDao, AuthDAO authDao) {
        this.gameDao = gameDao;
        this.authDao = authDao;
    }

    public Collection<GameData> listGames(String authToken) throws DataAccessException, ResponseException {
        authenticate(authToken);

        Collection<GameData> games = gameDao.getGames();
        if (games == null) {
            throw new DataAccessException("No games list");
        }

        return games;
    }

    public int createGame(String authToken, String gameName) throws DataAccessException, ResponseException {
        authenticate(authToken);

        GameData game = gameDao.createGame(gameName);
        if (game == null) {
            throw new DataAccessException("Game creation failed");
        }

        return game.gameID();
    }

    public boolean joinGame(String authToken, String gameID, String playerColor) throws DataAccessException, ResponseException {
        return false;
    }

    public boolean clear() throws DataAccessException {
        boolean done = gameDao.clearGames();
        if (!done) {
            throw new DataAccessException("Error: unable to clear game data");
        }

        return true;
    }

    private void authenticate(String authToken) throws DataAccessException, ResponseException {
        AuthData auth = authDao.getAuth(authToken);
        if (auth == null) {
            throw new ResponseException("Unauthorized");
        }
    }
}
