package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.MemoryUserSQL;
import dataAccess.UserDAOInterface;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



public class UserDAOTest {

  private UserDAOInterface userDAO;

  @BeforeEach
  void setUp() throws DataAccessException {
    userDAO = new MemoryUserSQL ();
  }

  @Test
  void createUserAndGetUser() throws DataAccessException {
    UserData user = new UserData("testUser", "password", "test@example.com");
    userDAO.createUser(user);

    UserData retrievedUser = userDAO.getUser("testUser");

    assertNotNull(retrievedUser);
    assertEquals(user, retrievedUser);
  }

  @Test
  void getAllUsers() throws DataAccessException {
    UserData user1 = new UserData("user1", "password1", "user1@example.com");
    UserData user2 = new UserData("user2", "password2", "user2@example.com");
    UserData user3 = new UserData("user3", "password3", "user3@example.com");

    userDAO.createUser(user1);
    userDAO.createUser(user2);
    userDAO.createUser(user3);

    var allUsers = userDAO.getAllUsers();

    assertNotNull(allUsers);
    assertTrue(allUsers.contains(user1));
    assertTrue(allUsers.contains(user2));
    assertTrue(allUsers.contains(user3));
  }

  @Test
  void clear() throws DataAccessException {
    UserData user = new UserData("testUser", "password", "test@example.com");
    userDAO.createUser(user);

    userDAO.clear();

    assertTrue(userDAO.getAllUsers().isEmpty());
  }
}
