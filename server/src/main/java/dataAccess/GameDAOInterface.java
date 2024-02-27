package dataAccess;

import model.GameData;

import java.util.List;

public interface GameDAOInterface {

  void createGame(GameData game);
  GameData getGame(int gameID);

  void updateGame(GameData game);

  List<GameData> getAllGames();
  void clear();
}
