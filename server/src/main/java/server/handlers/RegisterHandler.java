package server.handlers;

import dataaccess.DataAccessException;
import model.UserData;
import service.UserService;
import spark.*;

public class RegisterHandler implements Route{
    private final UserService userService;

    public RegisterHandler(UserService userService) {
        this.userService = userService;
    }

    public Object handle(Request req, Response res) {
        var user = JsonUsage.fromJson(req.body(), UserData.class);
        String message;
        if (user.username() == null || user.username().isEmpty() || user.password() == null || user.password().isEmpty() || user.email() == null || user.email().isEmpty()) {
            res.status(400);
            message = JsonUsage.getJson(new Message("Error: bad request"));
            return message;
        }

        try {
            var auth = userService.register(user);
            message = JsonUsage.getJson(auth);
        } catch (ResponseException e) {
            res.status(403);
            message = JsonUsage.getJson(new Message(e.getMessage()));
        } catch (DataAccessException e) {
            res.status(500);
            message = JsonUsage.getJson(new Message(e.getMessage()));
        }

        return message;
    }
}
