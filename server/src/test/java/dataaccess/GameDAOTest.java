package dataaccess;

import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class GameDAOTest {
    private static GameDAO gd;
    private static UserDAO ud;
    private static String name;
    private static int id;
    private static String username;

    @BeforeAll
    public static void setUp() throws Exception{
        ud = new MySqlUserDAO();
        gd = new MySqlGameDAO();
        name = "fun game";
        username = "joe";
        ud.createUser(new UserData(username, "password", "email@email.email"));
    }

    @BeforeEach
    public void initialize() throws Exception {
        gd.clearGames();
    }

    @AfterAll
    public static void cleanUp() throws Exception {
        gd.clearGames();
        ud.clearUsers();
    }

    @Test
    void getNameOutputTest() throws Exception {
        id = gd.createGame(name).gameID();
        assertInstanceOf(GameData.class, gd.getGameByName(name));
        assertEquals(id, gd.getGameByName(name).gameID());
    }

    @Test
    void getNameErrorTest() throws Exception {
        assertNull(gd.getGameByName(name));
    }

    @Test
    void getIdOutputTest() throws Exception {
        id = gd.createGame(name).gameID();
        assertInstanceOf(GameData.class, gd.getGameByID(id));
        assertEquals(name, gd.getGameByID(id).gameName());
    }

    @Test
    void getIdErrorTest() throws Exception {
        assertNull(gd.getGameByID(id));
    }

    @Test
    void createOutputTest() throws Exception {
        assertInstanceOf(GameData.class, gd.createGame(name));
    }

    @Test
    void createErrorTest() throws Exception {
        gd.createGame(name);
        assertNull(gd.createGame(name));
    }

    @Test
    void addOutputTest() throws Exception {
        GameData game = gd.createGame(name);
        assertTrue(gd.addPlayer(game, chess.ChessGame.TeamColor.WHITE, username));
    }

    @Test
    void addErrorTest() throws Exception {
        GameData game = gd.createGame(name);
        gd.addPlayer(game, chess.ChessGame.TeamColor.WHITE, username);
        assertFalse(gd.addPlayer(game, chess.ChessGame.TeamColor.WHITE, username));
    }

    @Test
    void updateOutputTest() throws Exception {
        GameData game = gd.createGame(name);
        GameData newGame = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
        assertTrue(gd.updateGame(newGame));
    }

    @Test
    void updateErrorTest() throws Exception {
        assertFalse(gd.updateGame(new GameData(id, username, null, name, null)));
    }

    @Test
    void clearTest() throws Exception {
        gd.createGame(name);
        assertTrue(gd.clearGames());
    }
}
