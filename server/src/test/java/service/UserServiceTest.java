package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.handlers.ResponseException;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private UserService us;
    private UserData user;

    @BeforeEach
    void setUp() {
        us = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());
        user = new UserData("joe schmo", "Sh0Tg[]n", "regular.joe@fakemail.com");
    }

    @Test
    void RegisterOutputTest() throws ResponseException, DataAccessException {
        var output = us.register(user);
        assertInstanceOf(AuthData.class, output);
        assertEquals(user.username(), output.username());
        assertNotNull(output.authToken());
    }

    @Test
    void RegisterTakenTest() {
        assertDoesNotThrow(() -> us.register(user));
        assertThrows(ResponseException.class, () -> us.register(user));
    }

    @Test
    void LoginNotFoundTest() {
        assertThrows(ResponseException.class, () -> us.login(user));
    }

    @Test
    void LoginOutputTest() throws ResponseException, DataAccessException {
        us.register(user);
        var output = us.login(user);
        assertInstanceOf(AuthData.class, output);
        assertEquals(user.username(), output.username());
        assertNotNull(output.authToken());
    }

    @Test
    void LogoutNotFoundTest() throws ResponseException, DataAccessException {
        var login = us.register(user);
        us.logout(login);
        assertThrows(ResponseException.class, () -> us.logout(login));
    }

    @Test
    void LogoutOutputTest() throws ResponseException, DataAccessException {
        var login = us.register(user);
        var output = us.logout(login);
        assertTrue(output);
    }

    @Test
    void ClearTest() throws ResponseException, DataAccessException {
        var login = us.register(user);
        assertDoesNotThrow(() -> us.clear());
        assertThrows(ResponseException.class, () -> us.login(user));
        assertThrows(ResponseException.class, () -> us.logout(login));
    }
}
