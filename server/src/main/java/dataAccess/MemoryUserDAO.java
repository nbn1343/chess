package dataAccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAOInterface{
  private HashMap<String, UserData> mapUser = new HashMap<>();

  @Override
  public void createUser(UserData user) {
    mapUser.put(user.username (), user);
  }

  @Override
  public UserData getUser(String userName) {
    return mapUser.get(userName);
  }

  @Override
  public void clear() {
    mapUser.clear();
  }
}