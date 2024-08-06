package client;

import model.*;
import org.junit.jupiter.api.*;
import request.JoinRequest;
import server.ResponseException;
import server.Server;
import server.ServerFacade;
import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:8080");
    }

    @BeforeEach
    public void clear() throws ResponseException {
        facade.clear("/db");
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerTest() throws ResponseException {
        var authData = facade.register(new UserData("player1", "password", "p1@email.com"));
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    public void registerError() {
        assertThrows(ResponseException.class, () -> {facade.register(null);});
    }

    @Test
    public void loginTest() throws ResponseException {
        AuthData auth = facade.register(new UserData("player1", "password", "p1@email.com"));
        facade.logout(auth.authToken());
        var authData = facade.login(new UserData("player1", "password", null));
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    public void loginError() {
        assertThrows(ResponseException.class, () -> {facade.login(null);});
    }

    @Test
    public void logoutTest() throws ResponseException {
        var authData = facade.register(new UserData("player1", "password", "p1@email.com"));
        assertDoesNotThrow(() -> {facade.logout(authData.authToken());});
    }

    @Test
    public void logoutError() {
        assertThrows(ResponseException.class, () -> {facade.logout(null);});
    }

    @Test
    public void listTest() throws ResponseException {
        var authData = facade.register(new UserData("player1", "password", "p1@email.com"));
        assertDoesNotThrow(() -> facade.listGames(authData.authToken()));
    }

    @Test
    public void listError() {
        assertThrows(ResponseException.class, () -> {facade.listGames(null);});
    }

    @Test
    public void createTest() throws ResponseException {
        var authData = facade.register(new UserData("player1", "password", "p1@email.com"));
        GameData game = new GameData(0, null, null, "game", null);
        assertDoesNotThrow(() -> facade.createGame(authData.authToken(), game).gameID());
    }

    @Test
    public void createError() {
        assertThrows(ResponseException.class, () -> {facade.createGame(null, null);});
    }

    @Test
    public void joinTest() throws ResponseException {
        var authData = facade.register(new UserData("player1", "password", "p1@email.com"));
        GameData game = new GameData(0, null, null, "game", null);
        GameData createdGame = facade.createGame(authData.authToken(), game);
        JoinRequest req = new JoinRequest(chess.ChessGame.TeamColor.WHITE, createdGame.gameID());
        assertDoesNotThrow(() -> facade.joinGame(authData.authToken(), req));
    }

    @Test
    public void joinError() {
        assertThrows(ResponseException.class, () -> {facade.joinGame(null, null);});
    }

    @Test
    public void clearTest() {
        assertDoesNotThrow(() -> {facade.clear("/db");});
    }

    @Test
    public void clearError() {
        assertThrows(ResponseException.class, () -> {facade.clear("/df");});
    }
}
