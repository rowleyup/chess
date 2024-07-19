package server.handlers;

import dataaccess.DataAccessException;
import model.GameData;
import service.GameService;
import spark.*;

public class CreateHandler implements Route{
    private final GameService gameService;

    public CreateHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public Object handle(Request req, Response res) {
        String auth = req.headers("authorization");
        String gameName = JsonUsage.fromJson(req.body(), GameData.class).gameName();
        String message;
        if (auth == null || auth.isEmpty() || gameName == null || gameName.isEmpty()) {
            res.status(400);
            message = JsonUsage.fromError("Error: bad request");
            return message;
        }

        try {
            int gameID = gameService.createGame(auth, gameName);
            message = JsonUsage.getJson(new GameData(gameID, null, null, null, null));
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
