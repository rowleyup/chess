package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.*;
import org.mindrot.jbcrypt.BCrypt;
import server.ResponseException;

/**
 * Contains user-related functions called by handlers
 */
public class UserService {
    private final UserDAO userDao;
    private final AuthDAO authDao;

    public UserService(UserDAO userDao, AuthDAO authDao) {
        this.userDao = userDao;
        this.authDao = authDao;
    }

    /**
     * Checks if username is already taken, calls createUser from UserDAO and createAuth from AuthDAO
     *
     * @param user is a UserData object containing a username, password, and email
     * @return an AuthData object containing the username and auth token
     * @throws ResponseException if username is already taken
     * @throws DataAccessException if createUser was unsuccessful
     */
    public AuthData register(UserData user) throws ResponseException, DataAccessException {
        if (userDao.getUser(user.username()) != null) {
            throw new ResponseException("Error: already taken");
        }

        String passwordHash = BCrypt.hashpw(user.password(), BCrypt.gensalt());

        boolean completed = userDao.createUser(new UserData(user.username(), passwordHash, user.email()));
        if (!completed) {
            throw new DataAccessException("Error: unable to create user");
        }

        return authDao.createAuth(user.username());
    }

    /**
     * Checks if user exists, calls createAuth from AuthDAO
     *
     * @param user is a UserData object containing a username and password
     * @return an AuthData object containing the username and auth token
     * @throws ResponseException if username or password is incorrect
     * @throws DataAccessException if thrown by createAuth
     */
    public AuthData login(UserData user) throws ResponseException, DataAccessException {
        UserData dbUser = userDao.getUser(user.username());
        if (dbUser == null) {
            throw new ResponseException("Error: unauthorized");
        }
        if (!BCrypt.checkpw(user.password(), dbUser.password())) {
            throw new ResponseException("Error: unauthorized");
        }

        return authDao.createAuth(user.username());
    }

    /**
     * Checks if user is logged in, calls removeAuth from AuthDAO
     *
     * @param authData is an AuthData object containing the auth token to be logged out
     * @return true if successful
     * @throws ResponseException if invalid auth token
     * @throws DataAccessException if removeAuth was unsuccessful
     */
    public boolean logout(AuthData authData) throws ResponseException, DataAccessException {
        AuthData user = authDao.getAuth(authData.authToken());
        if (user == null) {
            throw new ResponseException("Error: unauthorized");
        }

        boolean done = authDao.removeAuth(user);
        if (!done) {
            throw new DataAccessException("Error: unable to logout user");
        }
        return true;
    }

    /**
     * Calls clearUsers from UserDAO and clearAuth from AuthDAO, checks both for completion
     *
     * @return true if successful
     * @throws DataAccessException if clearUsers and/or clearAuth fails
     */
    public boolean clear() throws DataAccessException {
        boolean done = userDao.clearUsers();
        if (!done) {
            throw new DataAccessException("Error: unable to clear user data");
        }

        done = authDao.clearAuth();
        if (!done) {
            throw new DataAccessException("Error: unable to clear authentication data");
        }

        return true;
    }
}
