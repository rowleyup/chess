package dataaccess;

import model.UserData;
import java.sql.SQLException;

public class MySqlUserDAO implements UserDAO {
    public MySqlUserDAO() throws DataAccessException{
        TableCreator.configureDatabase("user");
    }

    public UserData getUser(String username) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM user WHERE username=?";
            try (var ps = connection.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String user = rs.getString("username");
                        String pass = rs.getString("password");
                        String email = rs.getString("email");
                        return new UserData(user, pass, email);
                    }
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: Unable to access database: %s", e.getMessage()));
        }
    }

    public boolean createUser(UserData user) throws DataAccessException {
        if (exists(user.username())) {
            return false;
        }

        try (var connection = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
            try (var ps = connection.prepareStatement(statement)) {
                ps.setString(1, user.username());
                ps.setString(2, user.password());
                ps.setString(3, user.email());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: Unable to access database: %s", e.getMessage()));
        }

        return true;
    }

    public boolean clearUsers() throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            var statement = "DELETE FROM user";
            try (var ps = connection.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: Unable to access database: %s", e.getMessage()));
        }

        return true;
    }

    private boolean exists(String username) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            var statement = "SELECT username FROM user WHERE username=?";
            try (var ps = connection.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: Unable to access database: %s", e.getMessage()));
        }
    }
}
