package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.*;
import server.handlers.ResponseException;

public class UserService {
    private final UserDAO userDao;
    private final AuthDAO authDao;

    public UserService(UserDAO userDao, AuthDAO authDao) {
        this.userDao = userDao;
        this.authDao = authDao;
    }

    public AuthData register(UserData user) throws ResponseException, DataAccessException {
        if (userDao.getUser(user.username()) != null) {
            throw new ResponseException("Error: already taken");
        }

        boolean completed = userDao.createUser(user);
        if (!completed) {
            throw new DataAccessException("Error: unable to create user");
        }

        return authDao.createAuth(user.username());
    }

    public AuthData login(UserData user) throws ResponseException, DataAccessException {
        return null;
    }

    public boolean logout(AuthData authData) throws ResponseException, DataAccessException {
        return false;
    }
}
