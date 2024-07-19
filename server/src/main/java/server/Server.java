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
        MemoryAuthDAO mem = new MemoryAuthDAO();
        userService = new UserService(new MemoryUserDAO(), mem);
        gameService = new GameService(new MemoryGameDAO(), mem);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
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
