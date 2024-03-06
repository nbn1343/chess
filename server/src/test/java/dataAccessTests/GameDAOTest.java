package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.MemoryGameSQL;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class GameDAOTest {

  private MemoryGameSQL gameDAO;

  @BeforeEach
  public void setUp() throws DataAccessException {
    gameDAO = new MemoryGameSQL();
    gameDAO.clear();
  }

  @Test
  void createGame_GetGameTest() throws DataAccessException {
    GameData gameData = new GameData(1, "whiteUser", "blackUser", "Chess", null);
    GameData gameData2 = new GameData(2, "whiteUser", "blackUser", "Chess", null);

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

  @Test
  void getNonExistingGame() throws DataAccessException {
    GameData gameData = new GameData(1, "whiteUser", "blackUser", "Chess", null);
    gameDAO.createGame(gameData);

    GameData retrievedGame = gameDAO.getGame(2);

    assertNull(retrievedGame, "Retrieved game should be null for a non-existing game ID");
  }

  @Test
  public void updateGameTest() throws DataAccessException {
    GameData originalGame = new GameData(1, "WhitePlayer", "BlackPlayer", "GameName", null);
    gameDAO.createGame(originalGame);

    GameData updatedGame = new GameData(originalGame.gameID(), "UpdatedWhitePlayer", "UpdatedBlackPlayer", "UpdatedGameName", null);

    gameDAO.updateGame(updatedGame);

    GameData retrievedGame = gameDAO.getGame(originalGame.gameID());

    assertEquals(updatedGame.gameID(), retrievedGame.gameID(), "Incorrect game ID");
    assertEquals(updatedGame.whiteUsername(), retrievedGame.whiteUsername(), "Incorrect white username");
    assertEquals(updatedGame.blackUsername(), retrievedGame.blackUsername(), "Incorrect black username");
    assertEquals(updatedGame.gameName(), retrievedGame.gameName(), "Incorrect game name");
    assertEquals(updatedGame.game(), retrievedGame.game(), "Incorrect game object");
  }

  @Test
  public void updateGameTest_NonExistingGame() throws DataAccessException {
    GameData originalGame = new GameData(1, "WhitePlayer", "BlackPlayer", "GameName", null);
    gameDAO.createGame(originalGame);

    GameData updatedGame = new GameData(2, "UpdatedWhitePlayer", "UpdatedBlackPlayer", "UpdatedGameName", null);

    gameDAO.updateGame(updatedGame);

    GameData retrievedGame = gameDAO.getGame(updatedGame.gameID());

    assertNull(retrievedGame, "Retrieved game should be null for a non-existing game ID");
  }

  @Test
  public void testGetAllGames() throws DataAccessException {
    GameData game1 = new GameData(1, "whiteUser1", "blackUser1", "Game1", null);
    GameData game2 = new GameData(2, "whiteUser2", "blackUser2", "Game2", null);

    gameDAO.createGame(game1);
    gameDAO.createGame(game2);

    Collection<GameData> allGames = gameDAO.getAllGames();

    assertEquals(2, allGames.size(), "Number of retrieved games should match the number of inserted games");
    assertTrue(allGames.contains(game1), "Game1 should be present in the retrieved games");
    assertTrue(allGames.contains(game2), "Game2 should be present in the retrieved games");
  }

  @Test
  public void testGetAllGames_EmptyGames() throws DataAccessException {
    Collection<GameData> allGames = gameDAO.getAllGames();

    assertTrue(allGames.isEmpty(), "There should be no games retrieved when the database is empty");
  }


  @Test
  void clearGames() throws DataAccessException {
    GameData game1 = new GameData(1, "whiteUser1", "blackUser1", "Game1", null);
    GameData game2 = new GameData(2, "whiteUser2", "blackUser2", "Game2", null);
    gameDAO.createGame(game1);
    gameDAO.createGame(game2);

    gameDAO.clear();

    Collection<GameData> allGames = gameDAO.getAllGames();
    assertTrue(allGames.isEmpty(), "No games should be present in the database after clearing");
  }

}
