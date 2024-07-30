package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.handlers.ResponseException;
import static org.junit.jupiter.api.Assertions.*;

public class AuthDAOTest {
    private static AuthDAO ad;
    private static UserDAO ud;
    private static String username;
    private static AuthData auth;

    @BeforeAll
    static void setUp() throws Exception {
        ud = new MySqlUserDAO();
        ad = new MySqlAuthDAO();
        username = "joe";
        ud.createUser(new UserData(username, "password", "email@email.email"));
    }

    @BeforeEach
    void initialize() throws Exception {
        auth = null;
        ad.clearAuth();
    }

    @AfterAll
    static void cleanUp() throws Exception {
        ad.clearAuth();
        ud.clearUsers();
    }

    @Test
    void createOutputTest() throws Exception {
        assertNotNull(ad.createAuth(username));
    }

    @Test
    void createErrorTest() {
        assertDoesNotThrow(() -> ad.createAuth(username));
        assertThrows(ResponseException.class, () -> ad.createAuth(username));
    }

    @Test
    void removeOutputTest() throws Exception {
        auth = ad.createAuth(username);
        assertTrue(ad.removeAuth(auth));
    }

    @Test
    void removeErrorTest() throws Exception {
        auth = new AuthData("joe", "aoiehtiKAGRIEKtgakls");
        assertFalse(ad.removeAuth(auth));
    }

    @Test
    void getOutputTest() throws Exception {
        auth = ad.createAuth(username);
        assertEquals(username, ad.getAuth(auth.authToken()).username());
    }

    @Test
    void getErrorTest() throws Exception {
        assertNull(ad.getAuth("alethakehdlsghads"));
    }

    @Test
    void clearTest() throws Exception {
        ad.createAuth(username);
        assertTrue(ad.clearAuth());
    }
}
