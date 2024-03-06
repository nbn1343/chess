package dataAccess;

import model.GameData;

import java.util.Collection;

public interface GameDAOInterface {

  GameData createGame(GameData game) throws DataAccessException;
  GameData getGame(int gameID) throws DataAccessException;

  void updateGame(GameData game) throws DataAccessException;

  Collection<GameData> getAllGames() throws DataAccessException;
  void clear() throws DataAccessException;
}
