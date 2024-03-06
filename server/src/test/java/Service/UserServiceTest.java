package Service;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCrypt;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

  private UserDAOInterface userDAO = new MemoryUserSQL ();
  private AuthDAOInterface authDAO = new MemoryAuthSQL ();
  private UserService userService = new UserService (userDAO, authDAO);

  UserServiceTest () throws DataAccessException {
  }

  @BeforeEach
  public void setUp () throws DataAccessException {
    userDAO = new MemoryUserSQL ();
    authDAO = new MemoryAuthSQL ();
    userService = new UserService (userDAO, authDAO);
    authDAO.clear ();
    userDAO.clear ();
  }

  @Test
  void successfulClear() throws DataAccessException {
    userService.register("User1", "password1", "user1@email.com");
    userService.register("User2", "password2", "user2@email.com");
    userService.register("User3", "password3", "user3@email.com");

    userService.clearData();

    assertTrue(userDAO.getAllUsers().isEmpty(), "UserDAO should be empty after clear");

    assertTrue(authDAO.getAllAuthData().isEmpty(), "AuthDAO should be empty after clear");
  }

  @Test
  void goodRegister() throws DataAccessException {
    userService.clearData();
    String username = "Username";
    String password = "password";
    String email = "test@email.com";

    AuthData returnData = userService.register(username, password, email);

    assertNotNull(returnData, "No data returned after registration");
    assertNotNull(returnData.authToken(), "No authToken returned after registration");
    assertNotNull(returnData.username(), "No username returned after registration");
    assertEquals(username, returnData.username(), "Registered username does not match");

    UserData storedUser = userDAO.getUser(username);
    assertNotNull(storedUser, "UserData not added to UserDAO after registration");
    assertEquals(username, storedUser.username(), "Stored username does not match");

    assertTrue(BCrypt.checkpw(password, storedUser.password()), "Password is not hashed correctly");

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
    Assertions.assertThrows(DataAccessException.class, () -> userService.register("Username", "password", "test@email.com"),
            "Error: already taken");
  }

  @Test
  void successfulLogin() throws DataAccessException {
    userService.register("Username", "password", "test@email.com");

    AuthData returnData = userService.login("Username", "password");

    Assertions.assertNotNull(returnData, "No data returned");
    Assertions.assertNotNull(returnData.authToken(), "No authToken returned");
    Assertions.assertNotNull(returnData.username(), "No username returned");
  }

  @Test
  void invalidCredentials() throws DataAccessException {
    userService.register("Username", "password", "test@email.com");

    Assertions.assertThrows(DataAccessException.class, () -> userService.login("Username", "wrongpassword"), "Login should fail with invalid password");
    Assertions.assertThrows(DataAccessException.class, () -> userService.login("NonexistentUser", "password"), "Login should fail with nonexistent username");
  }

  @Test
  void nullOrEmptyCredentials() {
    Assertions.assertThrows(DataAccessException.class, () -> userService.login(null, "password"), "Login should fail with null username");
    Assertions.assertThrows(DataAccessException.class, () -> userService.login("Username", null), "Login should fail with null password");
    Assertions.assertThrows(DataAccessException.class, () -> userService.login("", "password"), "Login should fail with empty username");
    Assertions.assertThrows(DataAccessException.class, () -> userService.login("Username", ""), "Login should fail with empty password");
  }

  @Test
  void successfulLogout() throws DataAccessException {
    userService.register("Username", "password", "test@email.com");

    AuthData authData = userService.login("Username", "password");
    String authToken = authData.authToken();

    userService.logout(authToken);

    assertNull(authDAO.getAuth(authToken), "AuthData should be removed from AuthDAO after logout");
  }
  @Test
  void invalidAuthTokenLogout() {
    String invalidAuthToken = "invalidAuthToken";

    DataAccessException exception = assertThrows(DataAccessException.class, () -> userService.logout(invalidAuthToken),
            "Logout with invalid authToken should throw DataAccessException");

    assertTrue(exception.getMessage().toLowerCase().contains("unauthorized"),
            "Exception message should indicate unauthorized access");
  }

}