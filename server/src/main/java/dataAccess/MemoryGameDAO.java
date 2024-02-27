package dataAccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.AuthData;
import model.GameData;

public class MemoryGameDAO implements GameDAOInterface {
  private HashMap<Integer, GameData> gameMap = new HashMap<>();
  private int nextGameID = 1;

  @Override
  public void createGame(GameData game) {
    gameMap.put(nextGameID++, game);
  }

  @Override
  public GameData getGame(int gameID) {
    return gameMap.get(gameID);
  }

  @Override
  public void updateGame(GameData game) {
    gameMap.put(game.gameID(), game);
  }

  public List<GameData> getAllGames() {
    return new ArrayList<> (gameMap.values());
  }


  @Override
  public void clear() {
    gameMap.clear();
    nextGameID = 1;
  }
}
