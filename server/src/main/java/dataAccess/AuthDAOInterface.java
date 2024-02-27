package dataAccess;

import model.AuthData;

import java.util.List;

public interface AuthDAOInterface {

  void createAuth(AuthData authData);
  AuthData getAuth(String authToken);

  List<AuthData> getAllAuthData();
  void deleteAuth(String authToken);

  String getUsername(String authToken);

  void clear();
}
