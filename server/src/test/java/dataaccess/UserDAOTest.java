package dataaccess;

import model.UserData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {
    public static UserDAO ud;
    public static String username;
    public static String password;
    public static String email;

    @BeforeAll
    public static void setUp() throws Exception {
        ud = new MySqlUserDAO();
        username = "joe";
        password = "password";
        email = "joe@gmail.com";
    }

    @BeforeEach
    public void initialize() throws Exception {
        ud.clearUsers();
    }

    @AfterAll
    public static void cleanUp() throws Exception {
        ud.clearUsers();
    }

    @Test
    void getOutputTest() throws Exception {
        ud.createUser(new UserData(username, password, email));
        assertEquals(username, ud.getUser(username).username());
    }

    @Test
    void getErrorTest() throws Exception {
        assertNull(ud.getUser(username));
    }

    @Test
    void createOutputTest() throws Exception {
        assertTrue(ud.createUser(new UserData(username, password, email)));
    }

    @Test
    void createErrorTest() throws Exception {
        ud.createUser(new UserData(username, password, email));
        assertFalse(ud.createUser(new UserData(username, password, email)));
    }

    @Test
    void clearTest() throws Exception {
        assertTrue(ud.clearUsers());
    }
}
