package server.handlers;

import dataaccess.DataAccessException;
import model.UserData;
import service.UserService;
import spark.*;

/**
 * Handles http request to login
 */
public class LoginHandler implements Route {
    private final UserService userService;

    public LoginHandler(UserService userService) {
        this.userService = userService;
    }

    public Object handle(Request req, Response res) {
        var user = JsonUsage.fromJson(req.body(), UserData.class);
        String message;

        /*
         * Check if Request contains a username and password
         */
        if (user.username() == null || user.username().isEmpty() || user.password() == null || user.password().isEmpty()) {
            res.status(400);
            message = JsonUsage.fromError("Error: bad request");
            return message;
        }

        /*
         * Run login function from UserService
         * Set status 401 from ResponseException if user does not exist
         * Set status 500 from DataAccessException if database error
         */
        try {
            var auth = userService.login(user);
            message = JsonUsage.getJson(auth);
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
