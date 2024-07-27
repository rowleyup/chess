package dataaccess;

import model.UserData;

public class MySqlUserDAO implements UserDAO {
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
