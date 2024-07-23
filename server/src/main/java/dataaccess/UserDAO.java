package dataaccess;

import model.UserData;

public interface UserDAO {

    /**
     * Returns the UserData for the specified user
     *
     * @param username is a string representing the username
     * @return a UserData object for the specified user, or null if the user does not exist
     * @throws DataAccessException if unable to read from database
     */
    UserData getUser(String username) throws DataAccessException;

    /**
     * Creates a new user using the given UserData
     *
     * @param user is a UserData object for the new user
     * @return true if successful, false if the user already exists
     * @throws DataAccessException if unable to update database
     */
    boolean createUser(UserData user) throws DataAccessException;

    /**
     * Clears all user information from the database
     *
     * @return true if successful
     * @throws DataAccessException if unable to update database
     */
    boolean clearUsers() throws DataAccessException;
}
