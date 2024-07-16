package dataaccess;

import model.UserData;

public interface UserDAO {
    UserData getUser(String username) throws DataAccessException;
    boolean createUser(UserData user) throws DataAccessException;
    boolean clearUsers() throws DataAccessException;
}
