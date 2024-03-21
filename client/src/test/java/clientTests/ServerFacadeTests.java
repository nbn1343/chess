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
    public void sampleTest () {
        Assertions.assertTrue (true);
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
    public void testListGames () {
        String username = "testUser";
        String password = "testPassword";
        String email = "test@example.com";

        serverFacade.register (username, password, email);
        serverFacade.login (username, password);
        // Test creating a game
        serverFacade.createGame ("TestGame");
        // Test listing available games
        String gamesList = serverFacade.listGames ();
        System.out.println ("Games List:\n" + gamesList);
        Assertions.assertNotNull (gamesList, "Failed to retrieve games list.");
    }

    @Test
    public void testRegister () {
        // Test registering a new account
        String username = "testUser";
        String password = "testPassword";
        String email = "test@example.com";

        AuthData authData = serverFacade.register (username, password, email);
        Assertions.assertNotNull (authData, "Failed to register a new account.");
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
}