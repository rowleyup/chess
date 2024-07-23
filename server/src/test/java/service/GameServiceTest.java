package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.*;
import org.junit.jupiter.api.*;
import server.handlers.ResponseException;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    private static GameService gs;
    private static AuthData auth;
    private static AuthData badAuth;
    private static String gameName;

    @BeforeAll
    static void setUp() throws ResponseException, DataAccessException {
        var mad = new MemoryAuthDAO();
        var user = new UserData("joe schmo", "Sh0Tg[]n", "regular.joe@fakemail.com");
        UserService us = new UserService(new MemoryUserDAO(), mad);
        gs = new GameService(new MemoryGameDAO(), mad);
        auth = us.register(user);
        badAuth = new AuthData("12345", "joe schmo");
        gameName = "fun game";
    }

    @Test
    @Order(3)
    void listNoAuthTest() {
        assertThrows(ResponseException.class, () -> {gs.listGames(badAuth.authToken());});
    }

    @Test
    @Order(4)
    @DisplayName("List Output Test")
    void listOutputTest() throws ResponseException, DataAccessException {
        assertNotNull(gs.listGames(auth.authToken()));
        assertEquals(1, gs.listGames(auth.authToken()).size());
    }

    @Test
    @Order(1)
    @DisplayName("Create - Bad Authentication Test")
    void createNoAuthTest() {
        assertThrows(ResponseException.class, () -> {gs.createGame(badAuth.authToken(), gameName);});
    }

    @Test
    @Order(2)
    @DisplayName("Create Output Test")
    void createOutputTest() throws ResponseException, DataAccessException {
        assertEquals(1111, gs.createGame(auth.authToken(), gameName));
    }

    @Test
    @Order(5)
    @DisplayName("Join - Bad Authentication Test")
    void joinNoAuthTest() {
        assertThrows(ResponseException.class, () -> {gs.joinGame(badAuth.authToken(), 1111, chess.ChessGame.TeamColor.WHITE);});
    }

    @Test
    @Order(6)
    @DisplayName("Join Output Test")
    void joinOutputTest() throws ResponseException, DataAccessException {
        assertTrue(gs.joinGame(auth.authToken(), 1111, chess.ChessGame.TeamColor.WHITE));
    }

    @Test
    @Order(7)
    @DisplayName("Join - Name Taken Test")
    void joinTakenTest() throws ResponseException, DataAccessException {
        assertFalse(gs.joinGame(auth.authToken(), 1111, chess.ChessGame.TeamColor.WHITE));
    }

    @Test
    @Order(8)
    @DisplayName("Game Clear Test")
    void clearTest() throws DataAccessException, ResponseException {
        assertTrue(gs.clear());
        assertEquals(0, gs.listGames(auth.authToken()).size());
    }
}
