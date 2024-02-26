package dataAccess;

import model.GameData;

public interface GameDAOInterface {

  void createGame(GameData game);
  GameData getGame(int gameID);
  void clear();
}
