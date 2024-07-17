package service;

import dataaccess.*;
import model.*;
import java.util.Collection;

public class GameService {
    private final GameDAO gameDao;
    private final AuthDAO authDao;

    public GameService(GameDAO gameDao, AuthDAO authDao) {
        this.gameDao = gameDao;
        this.authDao = authDao;
    }

    public Collection<GameData> listGames(String authToken) {
        return null;
    }

    public String createGame(String authToken, String gameName) {
        return null;
    }

    public boolean joinGame(String authToken, String gameID, String playerColor) {
        return false;
    }

    public boolean clear() {
        return false;
    }
}
