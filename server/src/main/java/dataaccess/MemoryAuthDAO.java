package dataaccess;

import model.AuthData;
import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    HashMap<String, String> authTokens;

    public MemoryAuthDAO() {
        authTokens = new HashMap<String, String>();
    }

    public AuthData createAuth(String username) throws DataAccessException {
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
