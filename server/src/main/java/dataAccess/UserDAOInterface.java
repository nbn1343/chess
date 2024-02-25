package dataAccess;

import model.UserData;

public interface UserDAOInterface {

  void createUser(UserData user);
  UserData getUser(String userName);
  void clear();
}
