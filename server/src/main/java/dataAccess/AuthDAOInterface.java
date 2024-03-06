package dataAccess;

import model.AuthData;

import java.util.List;

public interface AuthDAOInterface {

  void createAuth(AuthData authData) throws DataAccessException;
  AuthData getAuth(String authToken) throws DataAccessException;

  List<AuthData> getAllAuthData() throws DataAccessException;
  void deleteAuth(String authToken) throws DataAccessException;

  String getUsername(String authToken) throws DataAccessException;

  void clear() throws DataAccessException;
}
