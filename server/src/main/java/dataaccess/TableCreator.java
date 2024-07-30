package dataaccess;

import java.sql.SQLException;

public class TableCreator {
    static void configureDatabase(String table) throws DataAccessException {
        DatabaseManager.createDatabase();
        String[] createStatements = switch (table) {
            case "auth" -> authStatements;
            case "user" -> userStatements;
            case "game" -> gameStatements;
            case "gameUsers" -> usersInGameStatements;
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

    private static final String[] authStatements = {
            """
            CREATE TABLE IF NOT EXISTS auth (
            `username` varchar(50) NOT NULL,
            `authToken` varchar(256) NOT NULL,
            PRIMARY KEY (`authToken`),
            FOREIGN KEY (`username`) REFERENCES user(`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            """
    };

    private static final String[] userStatements = {
            """
            CREATE TABLE IF NOT EXISTS user (
            `username` varchar(50) NOT NULL,
            `password` varchar(256) NOT NULL,
            `email` varchar(256) NOT NULL,
            PRIMARY KEY (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            """
    };

    private static final String[] gameStatements = {
            """
            CREATE TABLE IF NOT EXISTS game (
            `id` int NOT NULL,
            `name` varchar(256) NOT NULL,
            `game` TEXT DEFAULT NULL,
            PRIMARY KEY (`id`),
            INDEX (`name`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            """
    };

    private static final String[] usersInGameStatements = {
            """
            CREATE TABLE IF NOT EXISTS users_in_game (
            `id` int NOT NULL,
            `white` varchar(50),
            `black` varchar(50),
            FOREIGN KEY (`id`) REFERENCES game(`id`),
            FOREIGN KEY (`white`) REFERENCES user(`username`),
            FOREIGN KEY (`black`) REFERENCES user(`username`),
            INDEX (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            """
    };
}
