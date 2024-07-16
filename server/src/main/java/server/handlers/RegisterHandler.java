package server.handlers;

import model.UserData;
import spark.*;
import com.google.gson.Gson;

public class RegisterHandler implements Route{
    public Object handle(Request req, Response res) throws ResponseException {
        var user = new Gson().fromJson(req.body(), UserData.class);
        var auth = service.UserService.register(user);
        return new Gson().toJson(auth);
    }
}
