package dataAccess;

import model.AuthData;


import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAOInterface{

  private final HashMap<String, AuthData> authMap = new HashMap<>();

  @Override
  public void createAuth(AuthData authData) {
    authMap.put(authData.authToken (), authData);
  }

  @Override
  public AuthData getAuth(String authToken) {
    return authMap.get(authToken);
  }

  @Override
  public void deleteAuth(String authToken) {
    authMap.remove(authToken);
  }

  @Override
  public void clear () {
    authMap.clear ();
  }

}
