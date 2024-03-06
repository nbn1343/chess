package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.GameDAOInterface;
import dataAccess.MemoryGameSQL;
import model.GameData;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class GameDAOTest {

  @Test
  void createGame() throws DataAccessException {
    GameData gameData = new GameData(1, "whiteUser", "blackUser", "Chess", null);
    GameData gameData2 = new GameData(2, "whiteUser", "blackUser", "Chess", null);

    GameDAOInterface gameDAO = new MemoryGameSQL ();

    gameDAO.clear ();

    gameDAO.createGame(gameData);
    gameDAO.createGame(gameData2);

    GameData retrievedGame = gameDAO.getGame(1);

    assertNotNull(retrievedGame, "Game not created successfully");
    assertEquals(gameData.gameID(), retrievedGame.gameID(), "Incorrect game ID");
    assertEquals(gameData.whiteUsername(), retrievedGame.whiteUsername(), "Incorrect white username");
    assertEquals(gameData.blackUsername(), retrievedGame.blackUsername(), "Incorrect black username");
    assertEquals(gameData.gameName(), retrievedGame.gameName(), "Incorrect game name");
    assertEquals(gameData.game(), retrievedGame.game(), "Incorrect game object");
  }
}
