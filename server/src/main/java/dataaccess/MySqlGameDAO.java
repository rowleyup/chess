package dataaccess;

import chess.ChessGame;
import model.GameData;
import server.ResponseException;
import server.JsonUsage;
import java.sql.*;
import java.util.Collection;
import java.util.HashSet;

public class MySqlGameDAO implements GameDAO {
    public MySqlGameDAO() throws DataAccessException {
        TableCreator.configureDatabase("game");
        TableCreator.configureDatabase("gameUsers");
    }

    public Collection<GameData> getGames() throws DataAccessException {
        var result = new HashSet<GameData>();

        try (var connection = DatabaseManager.getConnection()) {
            var statement = "SELECT id, name, game FROM game";
            try (var ps = connection.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readGame(rs));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: Unable to access database: %s", e.getMessage()));
        }

        return result;
    }

    public GameData getGameByName(String gameName) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            var statement = "SELECT id, name, game FROM game WHERE name = ?";
            try (var ps = connection.prepareStatement(statement)) {
                ps.setString(1, gameName);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: Unable to access database: %s", e.getMessage()));
        }
    }

    public GameData getGameByID(int gameID) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            var statement = "SELECT id, name, game FROM game WHERE id = ?";
            try (var ps = connection.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: Unable to access database: %s", e.getMessage()));
        }
    }

    public GameData createGame(String gameName) throws DataAccessException {
        if (exists(gameName)) {
            return null;
        }

        chess.ChessGame game = new chess.ChessGame();
        int id = 0;

        try (var connection = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO game(name, game) VALUES (?, ?)";
            try (var ps = connection.prepareStatement(statement)) {
                ps.setString(1, gameName);
                ps.setString(2, JsonUsage.getJson(game));
                ps.executeUpdate();
            }

            statement = "SELECT id FROM game WHERE name=?";
            try (var ps = connection.prepareStatement(statement)) {
                ps.setString(1, gameName);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        id = rs.getInt(1);
                    }
                }
            }

            statement = "INSERT INTO users_in_game(id, white, black) VALUES (?, ?, ?)";
            try (var ps = connection.prepareStatement(statement)) {
                ps.setInt(1, id);
                ps.setString(2, null);
                ps.setString(3, null);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: Unable to access database: %s", e.getMessage()));
        }

        return new GameData(id, null, null, gameName, game);
    }

    public boolean addPlayer(GameData game, ChessGame.TeamColor color, String username) throws DataAccessException, ResponseException {
        if (!exists(game.gameID())) {
            throw new ResponseException(String.format("Error: Game %s does not exist", game.gameID()));
        }

        if (taken(game, color)) {
            return false;
        }

        String statement;
        if (color == chess.ChessGame.TeamColor.BLACK) {
            statement = "UPDATE users_in_game SET black=? WHERE id=?";
        }
        else {
            statement = "UPDATE users_in_game SET white=? WHERE id=?";
        }

        try (var connection = DatabaseManager.getConnection()) {
            try (var ps = connection.prepareStatement(statement)) {
                ps.setString(1, username);
                ps.setInt(2, game.gameID());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: Unable to access database: %s", e.getMessage()));
        }

        return true;
    }

    public boolean clearGames() throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            var statement = "DELETE FROM game";
            try (var ps = connection.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: Unable to access database: %s", e.getMessage()));
        }

        return true;
    }

    public boolean updateGame(GameData game) throws DataAccessException {
        if (!exists(game.gameID())) {
            return false;
        }

        try (var connection = DatabaseManager.getConnection()) {
            var statement = "UPDATE game SET name=?, game=? WHERE id=?";
            try (var ps = connection.prepareStatement(statement)) {
                ps.setString(1, game.gameName());
                ps.setString(2, JsonUsage.getJson(game.game()));
                ps.setInt(3, game.gameID());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: Unable to access database: %s", e.getMessage()));
        }

        return true;
    }

    public boolean removeGame(int gameId) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            var statement = "DELETE FROM users_in_game WHERE id=?";
            try (var ps = connection.prepareStatement(statement)) {
                ps.setInt(1, gameId);
                ps.executeUpdate();
            }

            statement = "DELETE FROM game WHERE id=?";
            try (var ps = connection.prepareStatement(statement)) {
                ps.setInt(1, gameId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: Unable to access database: %s", e.getMessage()));
        }
        return true;
    }

    private boolean exists(int gameID) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            var statement = "SELECT id FROM game WHERE id=?";
            try (var ps = connection.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: Unable to access database: %s", e.getMessage()));
        }
    }

    private boolean exists(String gameName) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            var statement = "SELECT name FROM game WHERE name=?";
            try (var ps = connection.prepareStatement(statement)) {
                ps.setString(1, gameName);
                try (var rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: Unable to access database: %s", e.getMessage()));
        }
    }

    private GameData readGame(ResultSet rs) throws SQLException, DataAccessException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String json = rs.getString("game");
        chess.ChessGame game = JsonUsage.fromJson(json, chess.ChessGame.class);
        String white;
        String black;

        try (var connection = DatabaseManager.getConnection()) {
            var s = "SELECT white, black FROM users_in_game WHERE id=?";
            try (var p = connection.prepareStatement(s)) {
                p.setInt(1, rs.getInt("id"));
                try (var r = p.executeQuery()) {
                    if (r.next()) {
                        white = r.getString("white");
                        black = r.getString("black");
                    } else {
                        throw new DataAccessException("Error: Unable to find player data");
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: Unable to access database: %s", e.getMessage()));
        }

        return new GameData(id, white, black, name, game);
    }

    private boolean taken(GameData game, ChessGame.TeamColor color) throws DataAccessException {
        String statement;
        if (color == chess.ChessGame.TeamColor.BLACK) {
            statement = "SELECT black FROM users_in_game WHERE id=?";
        }
        else {
            statement = "SELECT white FROM users_in_game WHERE id=?";
        }

        try (var connection = DatabaseManager.getConnection()) {
            try (var ps = connection.prepareStatement(statement)) {
                ps.setInt(1, game.gameID());
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString(1) != null;
                    }
                    throw new DataAccessException("Error: Unable to find player data");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: Unable to access database: %s", e.getMessage()));
        }
    }
}
