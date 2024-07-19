package server.handlers;

import dataaccess.DataAccessException;
import model.GameData;
import server.RequestResponse.ListResponse;
import service.GameService;
import spark.*;

import java.util.Collection;

public class ListHandler implements Route{
    private final GameService gameService;

    public ListHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public Object handle(Request req, Response res) {
        String auth = req.headers("authorization");
        String message;
        if (auth == null || auth.isEmpty()) {
            res.status(400);
            message = JsonUsage.fromError("Error: bad request");
            return message;
        }

        try {
            Collection<GameData> games = gameService.listGames(auth);
            ListResponse gamesRes = new ListResponse(games);
            message = JsonUsage.getJson(gamesRes);
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
