package dataaccess;

import model.UserData;

import java.sql.SQLException;

public class MySqlUserDAO implements UserDAO {
    public MySqlUserDAO() throws DataAccessException{
        TableCreator.configureDatabase("user");
    }

    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    public boolean createUser(UserData user) throws DataAccessException {
        return false;
    }

    public boolean clearUsers() throws DataAccessException {
        return false;
    }
}
