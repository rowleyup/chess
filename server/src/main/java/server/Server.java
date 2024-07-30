package server;

import dataaccess.MySqlAuthDAO;
import dataaccess.MySqlGameDAO;
import dataaccess.MySqlUserDAO;
import server.handlers.*;
import service.*;
import spark.*;
import static java.lang.System.exit;

public class Server {
    private UserService userService;
    private GameService gameService;

    public Server() {
        try {
            var user = new MySqlUserDAO();
            var auth = new MySqlAuthDAO();
            userService = new UserService(user, auth);
            gameService = new GameService(new MySqlGameDAO(), auth);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints here.
        Spark.delete("/db", new ClearHandler(userService, gameService));
        Spark.post("/user", new RegisterHandler(userService));
        Spark.post("/session", new LoginHandler(userService));
        Spark.delete("/session", new LogoutHandler(userService));
        Spark.get("/game", new ListHandler(gameService));
        Spark.post("/game", new CreateHandler(gameService));
        Spark.put("/game", new JoinHandler(gameService));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
