package Service;

import dataAccess.DataAccessException;
import dataAccess.GameDAOInterface;
import dataAccess.MemoryGameDAO;
import dataAccess.AuthDAOInterface;
import dataAccess.MemoryAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.*;
import service.GameService;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

  private GameDAOInterface gameDAO;
  private AuthDAOInterface authDAO;
  private GameService gameService;

  @BeforeEach
  public void setUp() {
    gameDAO = new MemoryGameDAO();
    authDAO = new MemoryAuthDAO();
    gameService = new GameService(gameDAO, authDAO);
  }

  @Test
  void successfulCreateGame() throws DataAccessException {
    AuthData authData = new AuthData("validAuthToken", "username");
    authDAO.createAuth(authData);

    int gameID = gameService.createGame("whiteUser", "blackUser", "Chess", null, "validAuthToken");

    // Check if the game was successfully created
    assertNotNull(gameDAO.getGame(gameID), "Game not created successfully");
  }

  @Test
  void createGameWithInvalidAuthToken() {
    // Attempt to create a game with an invalid authToken
    assertThrows(DataAccessException.class, () ->
                    gameService.createGame("whiteUser", "blackUser", "Chess", null, "invalidAuthToken"),
            "Creating game with invalid authToken should throw DataAccessException");
  }
}

