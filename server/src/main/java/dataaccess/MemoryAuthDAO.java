package dataaccess;

import model.AuthData;
import server.handlers.ResponseException;

import java.util.HashMap;
import java.util.UUID;

/**
 * Implements AuthDAO and stores data in memory =
 */
public class MemoryAuthDAO implements AuthDAO {
    HashMap<String, String> authTokens;

    public MemoryAuthDAO() {
        authTokens = new HashMap<String, String>();
    }

    public AuthData createAuth(String username) throws ResponseException{
        if (authTokens.containsValue(username)) {
            throw new ResponseException("Error: already taken");
        }

        String token = UUID.randomUUID().toString();
        authTokens.put(token, username);
        return new AuthData(username, token);
    }

    public boolean removeAuth(AuthData authData) {
        String token = authData.authToken();
        if (!authTokens.containsKey(token)) {
            return false;
        }
        authTokens.remove(token);
        return true;
    }

    public AuthData getAuth(String authToken) {
        String username = authTokens.get(authToken);
        if (username == null) {
            return null;
        }

        return new AuthData(username, authToken);
    }

    public boolean clearAuth() {
        authTokens.clear();
        return true;
    }
}
