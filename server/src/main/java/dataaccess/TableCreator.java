package dataaccess;

import java.sql.SQLException;

public class TableCreator {
    static void configureDatabase(String table) throws DataAccessException {
        DatabaseManager.createDatabase();
        String[] createStatements = switch (table) {
            case "auth" -> AUTHSTATEMENTS;
            case "user" -> USERSTATEMENTS;
            case "game" -> GAMESTATEMENTS;
            case "gameUsers" -> PLAYERSTATEMENTS;
            default -> throw new DataAccessException(String.format("Error: Unknown table: %s", table));
        };

        try (var connection = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = connection.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: Unable to configure database: %s", e.getMessage()));
        }
    }

    private static final String[] AUTHSTATEMENTS = {
            """
            CREATE TABLE IF NOT EXISTS auth (
            `username` varchar(50) NOT NULL,
            `authToken` varchar(256) NOT NULL,
            PRIMARY KEY (`authToken`),
            FOREIGN KEY (`username`) REFERENCES user(`username`) ON DELETE CASCADE
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            """
    };

    private static final String[] USERSTATEMENTS = {
            """
            CREATE TABLE IF NOT EXISTS user (
            `username` varchar(50) NOT NULL,
            `password` varchar(256) NOT NULL,
            `email` varchar(256) NOT NULL,
            PRIMARY KEY (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            """
    };

    private static final String[] GAMESTATEMENTS = {
            """
            CREATE TABLE IF NOT EXISTS game (
            `id` int NOT NULL AUTO_INCREMENT,
            `name` varchar(256) NOT NULL,
            `game` TEXT DEFAULT NULL,
            PRIMARY KEY (`id`),
            INDEX (`name`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            """
    };

    private static final String[] PLAYERSTATEMENTS = {
            """
            CREATE TABLE IF NOT EXISTS users_in_game (
            `id` int NOT NULL,
            `white` varchar(50),
            `black` varchar(50),
            FOREIGN KEY (`id`) REFERENCES game(`id`) ON DELETE CASCADE,
            FOREIGN KEY (`white`) REFERENCES user(`username`) ON DELETE CASCADE,
            FOREIGN KEY (`black`) REFERENCES user(`username`) ON DELETE CASCADE,
            INDEX (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            """
    };
}
