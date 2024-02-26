package dataAccess;

import model.AuthData;


import java.util.HashMap;

public class MemoryAuthDAO {

  HashMap<String, AuthData> mapAuth = new HashMap<> ();

  public void clear() {
    mapAuth.clear();
  }
}
