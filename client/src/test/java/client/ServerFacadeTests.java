package client;

import model.*;
import org.junit.jupiter.api.*;
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
        facade.clear();
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
        facade.logout(facade.register(new UserData("player1", "password", "p1@email.com")).authToken());
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
    public void listTest() throws ResponseException {}

    @Test
    public void listError() throws ResponseException {}

    @Test
    public void createTest() throws ResponseException {}

    @Test
    public void createError() throws ResponseException {}

    @Test
    public void joinTest() throws ResponseException {}

    @Test
    public void joinError() throws ResponseException {}
}
