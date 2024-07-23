package server.handlers;

import dataaccess.DataAccessException;
import service.GameService;
import server.RequestResponse.JoinRequest;
import spark.*;

/**
 * Handles the http request to join a game
 */
public class JoinHandler implements Route{
    private final GameService gameService;

    public JoinHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public Object handle(Request req, Response res) {
        String auth = req.headers("authorization");
        JoinRequest joinReq = JsonUsage.fromJson(req.body(), JoinRequest.class);
        String message;

        /*
         * Check if Request contains an auth token and a valid team color
         */
        if (auth == null || auth.isEmpty() || joinReq.playerColor() == null) {
            res.status(400);
            message = JsonUsage.fromError("Error: bad request");
            return message;
        }

        /*
         * Run joinGame function from GameService and check that the operation was successful
         * Set status 403 if the color is already taken
         * Set status 401 from ResponseException if user is unauthorized
         * Set status 400 from DataAccessException if game does not exist
         * Set status 500 from DataAccessException if database error
         */
        try {
            boolean done = gameService.joinGame(auth, joinReq.gameID(), joinReq.playerColor());
            if (done) {
                message = "{}";
            }
            else {
                res.status(403);
                message = JsonUsage.fromError("Error: already taken");
            }
        } catch (ResponseException e) {
            res.status(401);
            message = JsonUsage.fromError(e.getMessage());
        } catch (DataAccessException e) {
            String m = e.getMessage();
            if (m.equals("Game not found")) {
                res.status(400);
                message = JsonUsage.fromError("Error: bad request");
            }
            else {
                res.status(500);
                message = JsonUsage.fromError(m);
            }
        }

        return message;
    }
}
