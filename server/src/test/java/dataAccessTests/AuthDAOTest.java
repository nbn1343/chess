package dataAccessTests;

import dataAccess.AuthDAOInterface;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthSQL;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AuthDAOTest {

  private AuthDAOInterface authDAO;

  @BeforeEach
  public void setUp() throws DataAccessException {
    authDAO = new MemoryAuthSQL();
    authDAO.clear();
  }

  @Test
  void createAndGetAuth() throws DataAccessException {
    AuthData authData = new AuthData("authToken123", "username123");
    authDAO.createAuth(authData);

    AuthData retrievedAuthData = authDAO.getAuth("authToken123");

    assertNotNull(retrievedAuthData);
    assertEquals(authData, retrievedAuthData);
  }

  @Test
  void createAndGetAuth_NonExistingAuthToken() throws DataAccessException {
    AuthData retrievedAuthData = authDAO.getAuth("nonExistingToken");

    assertNull(retrievedAuthData);
  }

  @Test
  void deleteAuth() throws DataAccessException {
    AuthData authData = new AuthData("authToken123", "username123");
    authDAO.createAuth(authData);

    authDAO.deleteAuth("authToken123");

    AuthData retrievedAuthData = authDAO.getAuth("authToken123");
    assertNull(retrievedAuthData);
  }

  @Test
  void deleteAuth_NonExistingAuthToken() throws DataAccessException {
    assertDoesNotThrow(() -> authDAO.deleteAuth("nonExistingToken"));
  }

  @Test
  void clear() throws DataAccessException {
    AuthData authData1 = new AuthData("authToken1", "username1");
    AuthData authData2 = new AuthData("authToken2", "username2");
    authDAO.createAuth(authData1);
    authDAO.createAuth(authData2);

    authDAO.clear();

    List<AuthData> allAuthData = authDAO.getAllAuthData();
    assertTrue(allAuthData.isEmpty(), "All authentication data should be cleared");
  }
}
