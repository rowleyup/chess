package server.handlers;

import dataaccess.DataAccessException;
import service.GameService;
import service.UserService;
import spark.*;

/**
 * Handles the http request to clear the database
 */
public class ClearHandler implements Route {
    private final UserService userService;
    private final GameService gameService;

    public ClearHandler(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    public Object handle(Request req, Response res) {
        String message = null;

        /*
         * Run clear functions from UserService and GameService and check that operations were successful
         * Set status 500 if unsuccessful or from DataAccessException
         */
        try {
            boolean done = userService.clear();
            if (!done) {
                res.status(500);
                message = JsonUsage.fromError("Error: unable to clear user and authentication data");
            }

            done = gameService.clear();
            if (!done) {
                res.status(500);
                message = JsonUsage.fromError("Error: unable to clear game data");
            }
        } catch (DataAccessException e) {
            res.status(500);
            message = JsonUsage.fromError(e.getMessage());
        }

        if (message == null) {
            message = "{}";
        }
        return message;
    }
}
