package dataaccess;

import model.AuthData;
import server.ResponseException;

public interface AuthDAO {

    /**
     * Creates an authentication token for the given user
     * and stores it with the username
     *
     * @param username is the username to be associated with the created auth token
     * @return AuthData object containing the username and the auth token
     * @throws DataAccessException if unable to update database
     */
    AuthData createAuth(String username) throws DataAccessException, ResponseException;

    /**
     * Deletes an auth-token/username pair from the database, logging out the user
     *
     * @param authData is the AuthData object to be deleted
     * @return true if successful, false if the user is not logged in
     * @throws DataAccessException if unable to update database
     */
    boolean removeAuth(AuthData authData) throws DataAccessException;

    /**
     * Retrieves the AuthData object from the database that is associated with the given token
     *
     * @param authToken is a string containing an auth token
     * @return the desired AuthData object, or null if not found
     * @throws DataAccessException if unable to read from database
     */
    AuthData getAuth(String authToken) throws DataAccessException;

    /**
     * Deletes all auth-token/username pairs in the database
     *
     * @return true if successful
     * @throws DataAccessException if unable to update database
     */
    boolean clearAuth() throws DataAccessException;
}
