package Service;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

  private UserDAOInterface userDAO = new MemoryUserDAO ();
  private AuthDAOInterface authDAO = new MemoryAuthDAO ();
  private UserService userService = new UserService (userDAO, authDAO);

  @BeforeEach
  public void setUp () {
    userDAO = new MemoryUserDAO ();
    authDAO = new MemoryAuthDAO ();
    userService = new UserService (userDAO, authDAO);
  }

  @Test
  void goodRegister() throws DataAccessException {
    // Arrange: Create a user
    UserData user = new UserData("Username", "password", "test@email.com");

    // Act: Register the user
    AuthData returnData = userService.register("Username", "password", "test@email.com");

    // Assert: Verify registration was successful
    assertNotNull(returnData, "No data returned after registration");
    assertNotNull(returnData.authToken(), "No authToken returned after registration");
    assertNotNull(returnData.username(), "No username returned after registration");
    assertEquals(user.username(), returnData.username(), "Registered username does not match");
    assertEquals(user, userDAO.getUser(user.username()), "UserData not added to UserDAO after registration");
    assertNotNull(authDAO.getAuth(returnData.authToken()), "AuthData not added to AuthDAO after registration");
  }


  @Test
  void badRegister() throws DataAccessException {
    Assertions.assertThrows(DataAccessException.class, () -> userService.register(null, "password", "test@email.com"), "Does not throw error");
    Assertions.assertThrows(DataAccessException.class, () -> userService.register("Username", null, "test@email.com"), "Does not throw error");
    Assertions.assertThrows(DataAccessException.class, () -> userService.register("Username", "password", null), "Does not throw error");

  }

  @Test
  void usernameTaken() throws DataAccessException {
    userService.register("Username", "password", "test@email.com");
    Assertions.assertThrows(DataAccessException.class, () -> userService.register("Username", "password", "test@email.com"), "Does not throw error");
  }

  @Test
  void successfulLogin() throws DataAccessException {
    // Register a user first
    userService.register("Username", "password", "test@email.com");

    // Perform login
    AuthData returnData = userService.login("Username", "password");

    // Verify login result
    Assertions.assertNotNull(returnData, "No data returned");
    Assertions.assertNotNull(returnData.authToken(), "No authToken returned");
    Assertions.assertNotNull(returnData.username(), "No username returned");
  }

  @Test
  void invalidCredentials() throws DataAccessException {
    // Register a user first
    userService.register("Username", "password", "test@email.com");

    // Perform login with invalid credentials
    Assertions.assertThrows(DataAccessException.class, () -> userService.login("Username", "wrongpassword"), "Login should fail with invalid password");
    Assertions.assertThrows(DataAccessException.class, () -> userService.login("NonexistentUser", "password"), "Login should fail with nonexistent username");
  }

  @Test
  void nullOrEmptyCredentials() {
    // Perform login with null or empty credentials
    Assertions.assertThrows(DataAccessException.class, () -> userService.login(null, "password"), "Login should fail with null username");
    Assertions.assertThrows(DataAccessException.class, () -> userService.login("Username", null), "Login should fail with null password");
    Assertions.assertThrows(DataAccessException.class, () -> userService.login("", "password"), "Login should fail with empty username");
    Assertions.assertThrows(DataAccessException.class, () -> userService.login("Username", ""), "Login should fail with empty password");
  }
}