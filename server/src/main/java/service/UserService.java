package service;

import model.UserData;
import dataAccess.UserDAOInterface;

public class UserService {
  private final UserDAOInterface userDAO;

  public UserService(UserDAOInterface userDAO) {
    this.userDAO = userDAO;
  }

  public void clearData() {
    userDAO.clear();
  }

  public void register(String username, String password, String email) {
    UserData user = new UserData (username, password, email);
    userDAO.createUser(user);
  }
}

