package dataAccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemoryAuthSQL implements AuthDAOInterface {

  public MemoryAuthSQL() throws DataAccessException {
    configureDatabase();
  }

  @Override
  public void createAuth(AuthData authData) throws DataAccessException {
    String sql = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, authData.authToken());
      stmt.setString(2, authData.username());
      stmt.executeUpdate();
    } catch (SQLException ex) {
      throw new DataAccessException("Error creating authentication data");
    }
  }

  @Override
  public AuthData getAuth(String authToken) throws DataAccessException {
    String sql = "SELECT * FROM auth WHERE authToken = ?";
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, authToken);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          String username = rs.getString("username");
          return new AuthData(authToken, username);
        } else {
          return null;
        }
      }
    } catch (SQLException ex) {
      throw new DataAccessException("Error retrieving authentication data");
    }
  }

  @Override
  public String getUsername(String authToken) throws DataAccessException {
    AuthData authData = getAuth(authToken);
    return authData != null ? authData.username() : null;
  }

  @Override
  public List<AuthData> getAllAuthData() throws DataAccessException {
    List<AuthData> authDataList = new ArrayList<>();
    String sql = "SELECT * FROM auth";
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
      while (rs.next()) {
        String authToken = rs.getString("authToken");
        String username = rs.getString("username");
        AuthData authData = new AuthData(authToken, username);
        authDataList.add(authData);
      }
    } catch (SQLException ex) {
      throw new DataAccessException("Error retrieving all authentication data");
    }
    return authDataList;
  }

  @Override
  public void deleteAuth(String authToken) throws DataAccessException {
    String sql = "DELETE FROM auth WHERE authToken = ?";
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, authToken);
      stmt.executeUpdate();
    } catch (SQLException ex) {
      throw new DataAccessException("Error deleting authentication data");
    }
  }

  @Override
  public void clear() throws DataAccessException {
    String sql = "TRUNCATE TABLE auth";
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.executeUpdate();
    } catch (SQLException ex) {
      throw new DataAccessException("Error clearing authentication data");
    }
  }

  private final String[] createStatements = {
          """
    CREATE TABLE IF NOT EXISTS auth (
            authToken VARCHAR(255) PRIMARY KEY,
            username VARCHAR(255) NOT NULL
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
