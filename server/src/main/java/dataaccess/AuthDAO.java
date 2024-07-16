package dataaccess;

import model.AuthData;

public interface AuthDAO {
    AuthData createAuth(String username) throws DataAccessException;
    boolean removeAuth(AuthData authData) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    boolean clearAuth() throws DataAccessException;
}
