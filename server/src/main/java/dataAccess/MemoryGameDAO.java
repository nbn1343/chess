package dataAccess;

import java.util.*;

import model.GameData;

public class MemoryGameDAO implements GameDAOInterface {
  private HashMap<Integer, GameData> gameMap = new HashMap<>();
  private int nextGameID = 1;

  @Override
  public GameData createGame(GameData game) {
    int gameId = nextGameID++;
    GameData updatedGame = new GameData (gameId,game.whiteUsername (),game.blackUsername (),game.gameName (),game.game ());
    gameMap.put(gameId, updatedGame);
    return updatedGame;
  }

  @Override
  public GameData getGame(int gameID) {
    return gameMap.get (gameID);
  }

  @Override
  public void updateGame(GameData game) {
    gameMap.put(game.gameID(), game);
    System.out.println (gameMap.put(game.gameID(), game));
  }

  public HashSet<GameData> getAllGames() {
    return new HashSet<> (gameMap.values());
  }


  @Override
  public void clear() {
    gameMap.clear();
    nextGameID = 1;
  }

  @Override
  public boolean equals (Object o) {
    if (this == o) return true;
    if (o == null || getClass () != o.getClass ()) return false;
    MemoryGameDAO that = (MemoryGameDAO) o;
    return nextGameID == that.nextGameID && Objects.equals (gameMap, that.gameMap);
  }

  @Override
  public int hashCode () {
    return Objects.hash (gameMap, nextGameID);
  }
}
