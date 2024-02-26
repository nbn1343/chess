package dataAccess;

import model.AuthData;

public interface AuthDAOInterface {

  void createAuth(AuthData authData);
  AuthData getAuth(String authToken);
  void deleteAuth(String authToken);

  void clear();
}
