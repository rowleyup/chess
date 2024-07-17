package dataaccess;

import model.AuthData;
import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    HashMap<String, String> authTokens;

    public MemoryAuthDAO() {
        authTokens = new HashMap<String, String>();
    }

    public AuthData createAuth(String username) throws DataAccessException {
        for (String name : authTokens.values()) {
            if (name.equals(username)) {
                throw new DataAccessException("Error: user already logged in");
            }
        }

        String token = UUID.randomUUID().toString();
        authTokens.put(token, username);
        return new AuthData(token, username);
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

        return new AuthData(authToken, username);
    }

    public boolean clearAuth() {
        authTokens.clear();
        return true;
    }
}
