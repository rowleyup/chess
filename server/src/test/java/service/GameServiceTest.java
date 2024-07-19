package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.*;
import org.junit.jupiter.api.*;
import server.handlers.ResponseException;

import java.util.HashSet;

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
    void ListNoAuthTest() {
        assertThrows(ResponseException.class, () -> {gs.listGames(badAuth.authToken());});
    }

    @Test
    @Order(4)
    void ListOutputTest() {
        assertNotNull(gs.listGames(auth.authToken()));
        assertEquals(1, gs.listGames(auth.authToken()).size());
    }

    @Test
    @Order(1)
    void CreateNoAuthTest() {
        assertThrows(ResponseException.class, () -> {gs.createGame(badAuth.authToken(), gameName);});
    }

    @Test
    @Order(2)
    void CreateOutputTest() {
        assertEquals(gameName, gs.createGame(auth.authToken(), gameName));
    }

    @Test
    @Order(5)
    void JoinNoAuthTest() {
        assertThrows(ResponseException.class, () -> {gs.joinGame(badAuth.authToken(), "1111", "WHITE");});
    }

    @Test
    @Order(6)
    void JoinOutputTest() {
        assertTrue(gs.joinGame(auth.authToken(), "1111", "WHITE"));
    }

    @Test
    @Order(7)
    void JoinTakenTest() {
        assertThrows(ResponseException.class, () -> {gs.joinGame(auth.authToken(), "1111", "WHITE");});
    }

    @Test
    @Order(8)
    void ClearTest() throws DataAccessException {
        assertTrue(gs.clear());
        assertNull(gs.listGames(auth.authToken()));
    }
}
