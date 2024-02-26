package service;

import dataAccess.AuthDAOInterface;
import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;
import dataAccess.UserDAOInterface;
import java.util.UUID;

public class UserService {
  private final UserDAOInterface userDAO;
  private final AuthDAOInterface authDAO;

  public UserService(UserDAOInterface userDAO, AuthDAOInterface authDAO) {
    this.userDAO = userDAO;
    this.authDAO = authDAO;
  }

  public void clearData() {
    userDAO.clear();
  }

  public AuthData register(String username, String password, String email) throws DataAccessException {
    if (userDAO.getUser(username) != null){
      throw new DataAccessException ("Username already taken");
    }
    String authToken = UUID.randomUUID().toString();

    UserData user = new UserData(username, password, email);
    AuthData authdata = new AuthData (authToken,username);

    userDAO.createUser(user);
    authDAO.createAuth (authdata);
    return authdata;
  }
}

