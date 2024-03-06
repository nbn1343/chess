//package dataAccess;
//
//import model.UserData;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//public class MemoryUserDAO implements UserDAOInterface{
//  private final HashMap<String, UserData> mapUser = new HashMap<>();
//
//  @Override
//  public void createUser(UserData user) {
//    mapUser.put(user.username (), user);
//  }
//
//  @Override
//  public UserData getUser(String userName) {
//    return mapUser.get(userName);
//  }
//
//  @Override
//  public List<UserData> getAllUsers() {
//    return new ArrayList<> (mapUser.values());
//  }
//
//  @Override
//  public void clear() {
//    mapUser.clear();
//  }
//
//
//}