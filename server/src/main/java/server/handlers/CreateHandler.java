package server.handlers;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.GameData;
import server.JsonUsage;
import server.ResponseException;
import service.GameService;
import spark.*;

/**
 * Handles the http request to create a game
 */
public class CreateHandler implements Route{
    private final GameService gameService;

    public CreateHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public Object handle(Request req, Response res) {
        String auth = req.headers("authorization");
        GameData gameData = JsonUsage.fromJson(req.body(), GameData.class);
        String message;

        /*
         * Check if Request contains an auth token and a game name
         */
        if (gameData == null) {
            res.status(400);
            message = JsonUsage.fromError("Error: bad request");
            return message;
        }
        String gameName = gameData.gameName();
        if (auth == null || auth.isEmpty() || gameName == null || gameName.isEmpty()) {
            res.status(400);
            message = JsonUsage.fromError("Error: bad request");
            return message;
        }

        /*
         * Run createGame function from GameService and check that the operation was successful
         * Set status 400 if unsuccessful, indicating the game does not exist
         * Set status 401 from ResponseException if user is not authorized
         * Set status 500 from DataAccessException if database error
         */
        try {
            int gameID = gameService.createGame(auth, gameName);

            if (gameID == -1) {
                res.status(400);
                message = JsonUsage.fromError("Error: game name already exists");
            }
            else {
                message = JsonUsage.getJson(new GameData(gameID, null, null, null, new ChessGame()));
            }
        } catch (ResponseException e) {
            res.status(401);
            message = JsonUsage.fromError(e.getMessage());
        } catch (DataAccessException e) {
            res.status(500);
            message = JsonUsage.fromError(e.getMessage());
        }

        return message;
    }
}
