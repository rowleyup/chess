package server.handlers;

import dataaccess.DataAccessException;
import model.*;
import service.*;
import spark.*;
import com.google.gson.Gson;

public class RegisterHandler implements Route{
    private final UserService userService;

    public RegisterHandler(UserService userService) {
        this.userService = userService;
    }

    public Object handle(Request req, Response res) {
        var user = new Gson().fromJson(req.body(), UserData.class);
        String message;
        if (user.username() == null || user.username().isEmpty() || user.password() == null || user.password().isEmpty() || user.email() == null || user.email().isEmpty()) {
            res.status(400);
            message = "{ \"message\": \"Error: bad request\" }";
            return message;
        }
        try {
            var auth = userService.register(user);
            message = new Gson().toJson(auth);
        } catch (ResponseException e) {
            res.status(403);
            message = "{ \"message\": \"" + e.getMessage() + "\" }";
        } catch (DataAccessException e) {
            res.status(500);
            message = "{ \"message\": \"" + e.getMessage() + "\" }";
        }
        return message;
    }
}
