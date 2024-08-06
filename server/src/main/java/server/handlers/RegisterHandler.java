package server.handlers;

import dataaccess.DataAccessException;
import model.UserData;
import server.JsonUsage;
import server.ResponseException;
import service.UserService;
import spark.*;

/**
 * Handles http request to register a new user
 */
public class RegisterHandler implements Route{
    private final UserService userService;

    public RegisterHandler(UserService userService) {
        this.userService = userService;
    }

    public Object handle(Request req, Response res) {
        var user = JsonUsage.fromJson(req.body(), UserData.class);
        String message;

        /*
         * Checks if Request contains a username, password, and email
         */
        if (user == null) {
            res.status(400);
            message = JsonUsage.fromError("Error: bad request");
            return message;
        }
        if (user.username() == null || user.username().isEmpty() || user.password() == null || user.password().isEmpty() ||
                user.email() == null || user.email().isEmpty()) {
            res.status(400);
            message = JsonUsage.fromError("Error: bad request");
            return message;
        }

        /*
         * Run register function from UserService
         * Set status 403 from ResponseException if user already taken
         * Set status 500 from DataAccessException if database error
         */
        try {
            var auth = userService.register(user);
            message = JsonUsage.getJson(auth);
        } catch (ResponseException e) {
            res.status(403);
            message = JsonUsage.fromError(e.getMessage());
        } catch (DataAccessException e) {
            res.status(500);
            message = JsonUsage.fromError(e.getMessage());
        }

        return message;
    }
}
