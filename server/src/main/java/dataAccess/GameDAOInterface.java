package dataAccess;

import model.GameData;

import java.util.Collection;
import java.util.List;

public interface GameDAOInterface {

  GameData createGame(GameData game);
  GameData getGame(int gameID);

  void updateGame(GameData game);

  Collection<GameData> getAllGames();
  void clear();
}
