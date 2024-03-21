package clientTests;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;
import dataAccess.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    private static MemoryAuthSQL authDAO;
    private static MemoryUserSQL userDAO;
    private static MemoryGameSQL gameDAO;


    @BeforeAll
    public static void init () throws DataAccessException {
        server = new Server ();
        var port = server.run (0);
        serverFacade = new ServerFacade (port);
        userDAO = new MemoryUserSQL ();
        authDAO = new MemoryAuthSQL ();
        gameDAO = new MemoryGameSQL ();
        userDAO.clear ();
        authDAO.clear ();
        gameDAO.clear ();
        System.out.println ("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer () {
        server.stop ();
    }


    @Test
    public void testRegister () {
        // Test registering a new account
        String username = "bob";
        String password = "testPassword";
        String email = "test@example.com";

        AuthData authData = serverFacade.register (username, password, email);
        Assertions.assertNotNull (authData, "Failed to register a new account.");
    }

    @Test
    public void testRegisterBad() {
        // Test registering with existing username/email
        String username = "existingUser";
        String password = "testPassword";
        String email = "existing@example.com";

        serverFacade.register(username, password, email);

        AuthData authData = serverFacade.register(username, password, email);

        Assertions.assertNull(authData, "Registration with existing username/email should fail.");
    }


    @Test
    public void testCreateGame () {
        String username = "testUser";
        String password = "testPassword";
        String email = "test@example.com";

        serverFacade.register (username, password, email);
        serverFacade.login (username, password);
        // Test creating a game
        boolean createSuccess = serverFacade.createGame ("TestGame");
        Assertions.assertTrue (createSuccess, "Failed to create a game.");
    }

    @Test
    public void testCreateGameBad() {
        // Test creating a game without logging in
        serverFacade.logout ();
        String gameName = "TestGame";

        boolean createSuccess = serverFacade.createGame(gameName);

        Assertions.assertFalse(createSuccess, "Game creation without logging in should fail.");
    }


    @Test
    public void testListGames () {
        String username = "testUser";
        String password = "testPassword";
        String email = "test@example.com";

        serverFacade.register (username, password, email);
        serverFacade.login (username, password);
        // Test creating a game
        serverFacade.createGame ("TestGame");
        String gamesList = serverFacade.listGames ();
        System.out.println ("Games List:\n" + gamesList);
        Assertions.assertNotNull (gamesList, "Failed to retrieve games list.");
    }

    @Test
    public void testListGamesBad() {
        // Test listing games without logging in
        serverFacade.logout ();

        String gamesList = serverFacade.listGames();

        String expectedMessage = "Failed to retrieve games list.";
        Assertions.assertEquals(expectedMessage,gamesList, "Listing games without logging in should fail.");
    }


    @Test
    public void testLogin () {
        // Test logging in after registration
        String username = "testUser";
        String password = "testPassword";
        String email = "test@example.com";

        serverFacade.register (username, password, email);
        AuthData authData = serverFacade.login (username, password);
        Assertions.assertNotNull (authData, "Failed to login.");
    }

    @Test
    public void testLoginBad() {
        // Test logging in with incorrect credentials
        String username = "testUser";
        String password = "wrongPassword";

        AuthData authData = serverFacade.login(username, password);

        Assertions.assertNull(authData, "Login with incorrect credentials should fail.");
    }


    @Test
    public void testJoinGame() {
        String username = "testUser";
        String password = "testPassword";
        String email = "test@example.com";

        serverFacade.register (username, password, email);
        serverFacade.login (username, password);
        serverFacade.createGame ("TestGame");
        boolean joinGameSuccess = serverFacade.joinGame (1,"white");
        Assertions.assertTrue (joinGameSuccess, "Failed to join a game.");

    }

    @Test
    public void testJoinGameBad() {
        // Test joining a non-existent game
        String username = "testUser";
        String password = "testPassword";
        String email = "test@example.com";

        serverFacade.register(username, password, email);
        serverFacade.login(username, password);
        serverFacade.createGame("TestGame");

        boolean joinGameSuccess = serverFacade.joinGame(999, "white");

        Assertions.assertFalse(joinGameSuccess, "Joining a non-existent game should fail.");
    }


    @Test
    public void testJoinSpecificGame() {
        String username = "testUser";
        String password = "testPassword";
        String email = "test@example.com";

        serverFacade.register (username, password, email);
        serverFacade.login (username, password);
        serverFacade.createGame ("TestGame");
        serverFacade.createGame ("TestGame1");
        serverFacade.createGame ("TestGame2");
        serverFacade.createGame ("JoinGame");
        boolean joinGameSuccess = serverFacade.joinGame (4,"white");
        Assertions.assertTrue (joinGameSuccess, "Failed to join a game.");

    }

    @Test
    public void testObserveGame() {
        String username = "testUser";
        String password = "testPassword";
        String email = "test@example.com";

        serverFacade.register (username, password, email);
        serverFacade.login (username, password);
        serverFacade.createGame ("TestGame");
        boolean joinGameSuccess = serverFacade.joinGameObserver (1);
        Assertions.assertTrue (joinGameSuccess, "Failed to join a game.");

    }

    @Test
    public void testObserveGameBad() {
        // Test observing a non-existent game
        String username = "testUser";
        String password = "testPassword";
        String email = "test@example.com";

        serverFacade.register(username, password, email);
        serverFacade.login(username, password);
        serverFacade.createGame("TestGame");

        boolean observeGameSuccess = serverFacade.joinGameObserver(999);

        Assertions.assertFalse(observeGameSuccess, "Observing a non-existent game should fail.");
    }



}