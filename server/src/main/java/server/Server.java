package server;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import server.handlers.*;
import service.*;
import spark.*;

public class Server {
    private final UserService userService;
    private final GameService gameService;

    public Server() {
        userService = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());
        gameService = new GameService(new MemoryGameDAO(), new MemoryAuthDAO());
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", new ClearHandler(userService, gameService));
        Spark.post("/user", new RegisterHandler(userService));
        Spark.post("/session", new LoginHandler(userService));
        Spark.delete("/session", new LogoutHandler(userService));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
