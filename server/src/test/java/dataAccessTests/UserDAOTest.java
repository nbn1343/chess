package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.MemoryUserSQL;
import dataAccess.UserDAOInterface;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCrypt;
import java.util.List;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import static org.junit.jupiter.api.Assertions.*;



public class UserDAOTest {

  private UserDAOInterface userDAO;

  @BeforeEach
  void setUp() throws DataAccessException {
    userDAO = new MemoryUserSQL ();
    userDAO.clear ();
  }

  @Test
  void createUserAndGetUser() throws DataAccessException {
    UserData user = new UserData("testUser", "password", "test@example.com");
    userDAO.createUser(user);

    UserData retrievedUser = userDAO.getUser("testUser");

    assertNotNull(retrievedUser);
    assertEquals(user.username(), retrievedUser.username());
    assertEquals(user.email(), retrievedUser.email());
    assertTrue(BCrypt.checkpw("password", retrievedUser.password()));
  }

  @Test
  void usernameExists() throws DataAccessException {
    UserData user = new UserData("existingUser", "password", "existing@example.com");
    userDAO.createUser(user);

    assertThrows(DataAccessException.class, () -> userDAO.createUser(user));
  }

  @Test
  void userNotFound() throws DataAccessException {
    UserData user = userDAO.getUser("nonExistentUser");
    assertNull(user);
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
    assertTrue(allUsers.stream().anyMatch(u -> u.username ().equals(user1.username ()) && u.email ().equals(user1.email ())));
    assertTrue(allUsers.stream().anyMatch(u -> u.username ().equals(user2.username ()) && u.email ().equals(user2.email ())));
    assertTrue(allUsers.stream().anyMatch(u -> u.username ().equals(user3.username ()) && u.email ().equals(user3.email ())));
  }

  @Test
  void noUsersExist() throws DataAccessException {
    List<UserData> allUsers = userDAO.getAllUsers();
    assertTrue(allUsers.isEmpty());
  }

  @Test
  void clear() throws DataAccessException {
    UserData user = new UserData("testUser", "password", "test@example.com");
    userDAO.clear();
    userDAO.createUser(user);

    userDAO.clear();

    assertTrue(userDAO.getAllUsers().isEmpty());
  }
}
