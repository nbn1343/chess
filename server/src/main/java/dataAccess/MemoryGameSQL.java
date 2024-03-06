package dataAccess;

import model.GameData;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

public class MemoryGameSQL implements GameDAOInterface{

  public MemoryGameSQL() throws DataAccessException {
    configureDatabase();
  }

  @Override
  public GameData createGame (GameData game) throws DataAccessException {
    String sql = "INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, game) VALUES(?,?,?,?,?) ";
    try (Connection conn = DatabaseManager.getConnection ();
         PreparedStatement stmt = conn.prepareStatement (sql)) {
      stmt.setInt (1, game.gameID ());
      stmt.setString (2, game.whiteUsername ());
      stmt.setString (3, game.blackUsername ());
      stmt.setString (4, game.gameName ());
      stmt.setObject (5, game.game ());
      stmt.executeUpdate ();
    } catch (SQLException ex) {
      throw new DataAccessException ("Error creating game");
    }
    return game;
  }

  @Override
  public GameData getGame (int gameID) throws DataAccessException{
    String sql = "SELECT * FROM games WHERE gameID = ?";
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt (1,gameID);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          String whiteUsername = rs.getString("whiteUsername");
          String blackUsername = rs.getString("blackUsername");
          String gameName = rs.getString("gameName");
          Object game = rs.getObject("game");


          return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
        } else {
          return null;
        }
      }
    } catch (SQLException ex) {
      throw new DataAccessException("Error retrieving game");
    }
  }

  @Override
  public void updateGame (GameData game) throws DataAccessException {
    String sql = "UPDATE games SET whiteUsername=?, blackUsername=?, gameName=?, game=? WHERE gameID=?";
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, game.whiteUsername());
      stmt.setString(2, game.blackUsername());
      stmt.setString(3, game.gameName());
      stmt.setObject(4, game.game());
      stmt.setInt(5, game.gameID());
      stmt.executeUpdate();
    } catch (SQLException ex) {
      throw new DataAccessException("Error updating game");
    }

  }

  @Override
  public Collection<GameData> getAllGames() throws DataAccessException {
    String sql = "SELECT * FROM games";
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
      Collection<GameData> games = new HashSet<> ();
      while (rs.next()) {
        int gameID = rs.getInt("gameID");
        String whiteUsername = rs.getString("whiteUsername");
        String blackUsername = rs.getString("blackUsername");
        String gameName = rs.getString("gameName");
        Object game = rs.getObject("game");
        games.add(new GameData(gameID, whiteUsername, blackUsername, gameName, game));
      }
      return games;
    } catch (SQLException ex) {
      throw new DataAccessException("Error retrieving games");
    }
  }

  @Override
  public void clear() throws DataAccessException {
    String sql = "DELETE FROM games";
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.executeUpdate();
    } catch (SQLException ex) {
      throw new DataAccessException("Error clearing games");
    }
  }

  private final String[] createStatements = {
          """
    CREATE TABLE IF NOT EXISTS games (
            gameID INT PRIMARY KEY,
            whiteUsername VARCHAR(255) NOT NULL,
            blackUsername VARCHAR(255) NOT NULL,
            gameName VARCHAR(255) NOT NULL,
            game BLOB
        )
    """
  };

  private void configureDatabase() throws DataAccessException {
    DatabaseManager.createDatabase();
    try (var conn = DatabaseManager.getConnection()) {
      for (var statement : createStatements) {
        try (var preparedStatement = conn.prepareStatement(statement)) {
          preparedStatement.executeUpdate();
        }
      }
    } catch (SQLException ex) {
      throw new DataAccessException("Unable to configure database: " + ex.getMessage());
    }
  }
}
