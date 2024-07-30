package dataaccess;

import model.AuthData;
import server.handlers.ResponseException;
import java.sql.SQLException;
import java.util.UUID;

public class MySqlAuthDAO implements AuthDAO {
    public MySqlAuthDAO() throws DataAccessException {
        TableCreator.configureDatabase("auth");
    }

    public AuthData createAuth(String username) throws DataAccessException, ResponseException {
        if (checkLoggedIn(username)) {
            throw new ResponseException("Error: User already logged in");
        }

        String token = UUID.randomUUID().toString();

        try (var connection = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO auth (username, authToken) VALUES (?, ?)";
            try (var ps = connection.prepareStatement(statement)) {
                ps.setString(1, username);
                ps.setString(2, token);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: Unable to access database: %s", e.getMessage()));
        }

        return new AuthData(username, token);
    }

    public boolean removeAuth(AuthData authData) throws DataAccessException {
        if (!checkLoggedIn(authData.username())) {
            return false;
        }

        try (var connection = DatabaseManager.getConnection()) {
            var statement = "DELETE FROM auth WHERE authToken = ?";
            try (var ps = connection.prepareStatement(statement)) {
                ps.setString(1, authData.authToken());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: Unable to access database: %s", e.getMessage()));
        }

        return true;
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            var statement = "SELECT username, authToken FROM auth WHERE authToken = ?";
            try (var ps = connection.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new AuthData(rs.getString("username"), rs.getString("authToken"));
                    }
                    else {
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: Unable to access database: %s", e.getMessage()));
        }
    }

    public boolean clearAuth() throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            var statement = "DELETE FROM auth";
            try (var ps = connection.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: Unable to access database: %s", e.getMessage()));
        }

        return true;
    }

    private boolean checkLoggedIn(String username) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            var statement = """
                    SELECT username
                    FROM auth
                    WHERE username=?
                    """;
            try (var prepStatement = connection.prepareStatement(statement)) {
                prepStatement.setString(1, username);
                try (var rs = prepStatement.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: Unable to access database: %s", e.getMessage()));
        }
    }
}
