package dataAccess;

import model.UserData;

import java.util.List;

public interface UserDAOInterface {

  void createUser(UserData user) throws DataAccessException;
  UserData getUser(String userName) throws DataAccessException;
  List<UserData> getAllUsers() throws DataAccessException;
  void clear() throws DataAccessException;

}
