//package dataAccess;
//
//import model.AuthData;
//
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//public class MemoryAuthDAO implements AuthDAOInterface{
//
//  private final HashMap<String, AuthData> authMap = new HashMap<>();
//
//  @Override
//  public void createAuth(AuthData authData) {
//    authMap.put(authData.authToken (), authData);
//  }
//
//  @Override
//  public AuthData getAuth(String authToken) {
//    return authMap.get(authToken);
//  }
//
//  @Override
//  public String getUsername(String authToken) {
//    AuthData authData = getAuth(authToken);
//    return authData != null ? authData.username() : null;
//  }
//
//  @Override
//  public List<AuthData> getAllAuthData() {
//    return new ArrayList<> (authMap.values());
//  }
//
//  @Override
//  public void deleteAuth(String authToken) {
//    authMap.remove(authToken);
//  }
//
//  @Override
//  public void clear () {
//    authMap.clear ();
//  }
//
//}
