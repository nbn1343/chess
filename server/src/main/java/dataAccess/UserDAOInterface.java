package dataAccess;

import model.UserData;

import java.util.List;

public interface UserDAOInterface {

  void createUser(UserData user);
  UserData getUser(String userName);
  List<UserData> getAllUsers();
  void clear();
}
