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
    private static UserService us;
    private static UserData user;

    @BeforeAll
    static void setUp() {
        us = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());
        user = new UserData("joe schmo", "Sh0Tg[]n", "regular.joe@fakemail.com");
    }

    @BeforeEach
    void reset() throws DataAccessException {
        us.clear();
    }

    @Test
    @DisplayName("Register Output Test")
    void registerOutputTest() throws ResponseException, DataAccessException {
        var output = us.register(user);
        assertInstanceOf(AuthData.class, output);
        assertEquals(user.username(), output.username());
        assertNotNull(output.authToken());
    }

    @Test
    @DisplayName("Register - Name Taken Test")
    void registerTakenTest() {
        assertDoesNotThrow(() -> us.register(user));
        assertThrows(ResponseException.class, () -> us.register(user));
    }

    @Test
    @DisplayName("Login - User Not Found Test")
    void loginNotFoundTest() {
        assertThrows(ResponseException.class, () -> us.login(user));
    }

    @Test
    @DisplayName("Login Output Test")
    void loginOutputTest() throws ResponseException, DataAccessException {
        var auth = us.register(user);
        us.logout(auth);
        var output = us.login(user);
        assertInstanceOf(AuthData.class, output);
        assertEquals(user.username(), output.username());
        assertNotNull(output.authToken());
    }

    @Test
    @DisplayName("Logout - User Not Found Test")
    void logoutNotFoundTest() throws ResponseException, DataAccessException {
        var login = us.register(user);
        us.logout(login);
        assertThrows(ResponseException.class, () -> us.logout(login));
    }

    @Test
    @DisplayName("Logout Output Test")
    void logoutOutputTest() throws ResponseException, DataAccessException {
        var login = us.register(user);
        var output = us.logout(login);
        assertTrue(output);
    }

    @Test
    @DisplayName("User Clear Test")
    void clearTest() throws ResponseException, DataAccessException {
        var login = us.register(user);
        assertDoesNotThrow(() -> us.clear());
        assertThrows(ResponseException.class, () -> us.login(user));
        assertThrows(ResponseException.class, () -> us.logout(login));
    }
}
