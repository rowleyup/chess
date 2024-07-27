package dataaccess;

import model.AuthData;
import server.handlers.ResponseException;

public class MySqlAuthDAO implements AuthDAO {
    public AuthData createAuth(String username) throws DataAccessException, ResponseException {
        return null;
    }

    public boolean removeAuth(AuthData authData) throws DataAccessException {
        return false;
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    public boolean clearAuth() throws DataAccessException {
        return false;
    }
}
