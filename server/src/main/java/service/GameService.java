package service;

import dataAccess.AuthDAOInterface;
import dataAccess.GameDAOInterface;
import dataAccess.DataAccessException;
import model.GameData;



public class GameService {
  private final GameDAOInterface gameDAO;
  private final AuthDAOInterface authDAO;

  public GameService(GameDAOInterface gameDAO, AuthDAOInterface authDAO) {
    this.gameDAO = gameDAO;
    this.authDAO = authDAO;
  }

  public int createGame(String whiteUsername, String blackUsername, String gameName, Object game, String authToken) throws DataAccessException {
    if (!isValidAuthToken(authToken)) {
      throw new DataAccessException("Error: unauthorized");
    }

    GameData newGame = new GameData(1, whiteUsername, blackUsername, gameName, game);
    gameDAO.createGame(newGame);
    return newGame.gameID ();
  }
  private boolean isValidAuthToken(String authToken) {
    return authDAO.getAuth(authToken) != null;
  }
}
