package dataAccess;

import model.UserData;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import dataAccess.UserDAOInterface;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class MemoryUserSQL implements UserDAOInterface {

  public MemoryUserSQL() throws DataAccessException {
    configureDatabase();
  }

  @Override
  public void createUser(UserData user) throws DataAccessException {
    String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
    try (Connection conn = DatabaseManager.getConnection();
    PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, user.username ());
      String hashedPassword = hashedPassword(user.password ());
      stmt.setString (2, hashedPassword);
      stmt.setString(3, user.email ());
      stmt.executeUpdate();
    } catch (SQLException ex) {
      throw new DataAccessException("Error creating user");
    }
  }

  @Override
  public UserData getUser(String username) throws DataAccessException {
    String sql = "SELECT * FROM users WHERE username = ?";
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, username);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          String password = rs.getString("password");
          String email = rs.getString("email");
          return new UserData(username, password, email);
        } else {
          return null;
        }
      }
    } catch (SQLException ex) {
      throw new DataAccessException("Error retrieving user");
    }
  }

  @Override
  public List<UserData> getAllUsers() throws DataAccessException {
    List<UserData> users = new ArrayList<>();
    String sql = "SELECT * FROM users";
    try (Connection conn = DatabaseManager.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        String username = rs.getString("username");
        String password = rs.getString("password");
        String email = rs.getString("email");
        users.add(new UserData(username, password, email));
      }
      return users;
    } catch (SQLException ex) {
      throw new DataAccessException("Error retrieving users");
    }
  }

  @Override
  public void clear() throws DataAccessException {
    String sql = "DELETE FROM users";
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.executeUpdate();
    } catch (SQLException ex) {
      throw new DataAccessException("Error clearing users");
    }
  }

  private final String[] createStatements = {
          """
    CREATE TABLE IF NOT EXISTS users (
      username VARCHAR(255) NOT NULL PRIMARY KEY,
      password VARCHAR(255) NOT NULL,
      email VARCHAR(255) NOT NULL
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

  private String hashedPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt());
  }
}
