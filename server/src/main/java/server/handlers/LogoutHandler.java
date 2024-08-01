package server.handlers;

import dataaccess.DataAccessException;
import model.AuthData;
import server.JsonUsage;
import server.ResponseException;
import service.UserService;
import spark.*;

/**
 * Handles http request to logout
 */
public class LogoutHandler implements Route {
    private final UserService userService;

    public LogoutHandler(UserService userService) {
        this.userService = userService;
    }

    public Object handle(Request req, Response res) {
        String auth = req.headers("authorization");
        var authToken = new AuthData(null, auth);
        String message;

        /*
         * Check if Request contains an auth token
         */
        if (authToken.authToken() == null || authToken.authToken().isEmpty()) {
            res.status(400);
            message = JsonUsage.fromError("Error: bad request");
            return message;
        }

        /*
         * Run logout function from UserService and check that the operation was successful
         * Set status 500 if unsuccessful or from DataAccessException
         * Set status 401 from ResponseException if user was not logged in
         */
        try {
            boolean done = userService.logout(authToken);
            if (done) {
                message = "{}";
            }
            else {
                res.status(500);
                message = JsonUsage.fromError("Error: unable to logout user");
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
