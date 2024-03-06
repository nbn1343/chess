package Service;

import dataAccess.DataAccessException;
import dataAccess.GameDAOInterface;
import dataAccess.MemoryGameSQL;
import dataAccess.AuthDAOInterface;
import dataAccess.MemoryAuthSQL;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import service.GameService;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

  private GameDAOInterface gameDAO;
  private AuthDAOInterface authDAO;
  private GameService gameService;

  @BeforeEach
  public void setUp () throws DataAccessException {
    gameDAO = new MemoryGameSQL ();
    authDAO = new MemoryAuthSQL ();
    gameService = new GameService (gameDAO, authDAO);
    authDAO.clear ();
    gameDAO.clear ();
  }

  @Test
  void successfulCreateGame() throws DataAccessException {
    AuthData authData = new AuthData("validAuthToken", "username");
    authDAO.createAuth(authData);
    GameData gameID = gameService.createGame(1,"whiteUser", "blackUser", "Chess", null, "validAuthToken");
    assertNotNull(gameDAO.getGame(gameID.gameID ()), "Game not created successfully");
  }

  @Test
  void createGameWithInvalidAuthToken() {
    assertThrows(DataAccessException.class, () ->
                    gameService.createGame(1,"whiteUser", "blackUser", "Chess", null, "invalidAuthToken"),
            "Creating game with invalid authToken should throw DataAccessException");
  }

  @Test
  void successfulJoinGame() throws DataAccessException {
    AuthData authData = new AuthData("validAuthToken", "username");
    authDAO.createAuth(authData);
    GameData gameData = gameService.createGame(1, "whiteUser", null, "Chess", null, "validAuthToken");
    gameService.joinGame("validAuthToken", gameData.gameID(), "BLACK");
    GameData updatedGameData = gameDAO.getGame(gameData.gameID());
    assertNotNull(updatedGameData);
    assertEquals("username", updatedGameData.blackUsername());
  }

  @Test
  void joinGameWithInvalidAuthToken() {
    assertThrows(DataAccessException.class, () ->
                    gameService.joinGame("invalidAuthToken", 1, "BLACK"),
            "Joining game with invalid authToken should throw DataAccessException");
  }

  @Test
  void joinGameWithInvalidGameID() {
    assertThrows(DataAccessException.class, () ->
                    gameService.joinGame("validAuthToken", 999, "BLACK"),
            "Joining non-existent game should throw DataAccessException");
  }

  @Test
  void successfulListGames() throws DataAccessException {
    AuthData authData = new AuthData("validAuthToken", "username");
    authDAO.createAuth(authData);

    gameService.createGame(1, "whiteUser1", "blackUser1", "Chess1", null, "validAuthToken");
    gameService.createGame(2, "whiteUser2", "blackUser2", "Chess2", null, "validAuthToken");

    Collection<GameData> games = gameService.listGames("validAuthToken");

    assertFalse(games.isEmpty(), "List of games should not be empty");

    assertEquals(2, games.size(), "Incorrect number of games listed");
  }

  @Test
  void listGamesWithInvalidAuthToken() {
    assertThrows(DataAccessException.class, () ->
                    gameService.listGames("invalidAuthToken"),
            "Listing games with invalid authToken should throw DataAccessException");
  }
}

