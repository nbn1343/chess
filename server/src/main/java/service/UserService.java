package service;

import dataAccess.AuthDAOInterface;
import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;
import dataAccess.UserDAOInterface;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.UUID;

public class UserService {
  private final UserDAOInterface userDAO;
  private final AuthDAOInterface authDAO;

  public UserService(UserDAOInterface userDAO, AuthDAOInterface authDAO) {
    this.userDAO = userDAO;
    this.authDAO = authDAO;
  }

  public void clearData() throws DataAccessException {
    userDAO.clear();
    authDAO.clear();
  }

  public AuthData register(String username, String password, String email) throws DataAccessException {
    if (userDAO.getUser(username) != null){
      throw new DataAccessException ("Error: already taken");
    }
    if (username == null | password == null | email == null) {
      throw new DataAccessException ("Error: bad request");
    }
    String authToken = UUID.randomUUID().toString();

    UserData user = new UserData(username, password, email);
    AuthData authdata = new AuthData (authToken,username);

    userDAO.createUser(user);
    authDAO.createAuth (authdata);
    return authdata;
  }

  public AuthData login(String username, String password) throws DataAccessException {
    UserData user = userDAO.getUser(username);
    if (user == null || !BCrypt.checkpw(password, user.password())) {
      throw new DataAccessException("Error: unauthorized");
    }
    String authToken = UUID.randomUUID().toString();
    AuthData authData = new AuthData(authToken, username);
    authDAO.createAuth(authData);
    return authData;
  }

  public void logout(String authToken) throws DataAccessException {
    if (authDAO.getAuth(authToken) == null || authToken.isEmpty()) {
      throw new DataAccessException("Error: unauthorized");
    }

    authDAO.deleteAuth(authToken);
  }



  public boolean isValidAuthToken(String authToken) throws DataAccessException {
    AuthData authData = authDAO.getAuth(authToken);
    return authData == null;
  }

}

