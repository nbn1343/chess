package service;

import dataAccess.AuthDAOInterface;
import dataAccess.GameDAOInterface;
import dataAccess.DataAccessException;
import model.GameData;
import java.util.Collection;



public class GameService {
  private final GameDAOInterface gameDAO;
  private final AuthDAOInterface authDAO;

  public GameService(GameDAOInterface gameDAO, AuthDAOInterface authDAO) {
    this.gameDAO = gameDAO;
    this.authDAO = authDAO;
  }

  public GameData createGame(int gameId, String whiteUsername, String blackUsername, String gameName, Object game, String authToken) throws DataAccessException {
    if (isValidAuthToken (authToken)) {
      throw new DataAccessException("Error: unauthorized");
    }

    GameData newGame = new GameData(gameId,whiteUsername, blackUsername, gameName, game);
    return gameDAO.createGame(newGame);
  }

  public Collection<GameData> listGames(String authToken) throws DataAccessException {
    if (isValidAuthToken (authToken)) {
      throw new DataAccessException("Error: unauthorized");
    }
    return gameDAO.getAllGames();
  }

  public void joinGame(String authToken, int gameID, String playerColor) throws DataAccessException {
    if (isValidAuthToken (authToken)) {
      throw new DataAccessException("Error: unauthorized");
    }

    GameData gameData = gameDAO.getGame(gameID);

    if (gameData == null) {
      throw new DataAccessException("Error: bad request");
    }

    if (("white".equals(playerColor) && gameData.whiteUsername() != null) ||
            ("black".equals(playerColor) && gameData.blackUsername() != null)) {
      throw new DataAccessException("Error: already taken");
    }

    if ("white".equals(playerColor)) {
      gameData = new GameData(gameID, authDAO.getUsername(authToken), gameData.blackUsername(), gameData.gameName(), gameData.game());
    } else if ("black".equals(playerColor)) {
      gameData = new GameData(gameID, gameData.whiteUsername(), authDAO.getUsername(authToken), gameData.gameName(), gameData.game());
    }

    gameDAO.updateGame(gameData);
  }
  private boolean isValidAuthToken(String authToken) throws DataAccessException {
    return authDAO.getAuth (authToken) == null;
  }

}


