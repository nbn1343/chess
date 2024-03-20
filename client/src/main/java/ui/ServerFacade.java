package ui;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import model. *;
public class ServerFacade {

  private final String serverUrl;
  private String authToken;


  public ServerFacade(String serverUrl) {
    this.serverUrl = serverUrl;
    authToken = null;
  }

  public AuthData login(String username, String password) {
    try {
      URL url = new URL(serverUrl + "/session");
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setDoOutput(true);

      // Create JSON object for login data
      String requestBody = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";


      // Write login data to request body
      OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
      writer.write(requestBody);
      writer.flush();

      // Read response
      BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      StringBuilder response = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        response.append(line);
      }
      reader.close();

      // Parse response
      AuthData authData = new Gson().fromJson(response.toString(), AuthData.class);
      authToken = authData.authToken ();
      return authData;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public AuthData register(String username, String password, String email) {
    try {
      URL url = new URL(serverUrl + "/user");
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setDoOutput(true);

      // Create JSON object for registration data
      UserData userData = new UserData(username, password, email);
      String requestBody = new Gson ().toJson(userData);

      // Write registration data to request body
      OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
      writer.write(requestBody);
      writer.flush();

      // Read response
      BufferedReader reader = new BufferedReader(new InputStreamReader (conn.getInputStream()));
      StringBuilder response = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        response.append(line);
      }
      reader.close();

      // Parse response
      AuthData authData = new Gson().fromJson(response.toString(), AuthData.class);
      authToken = authData.authToken();
      return authData;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public boolean logout() {
    try {
      URL url = new URL(serverUrl + "/session");
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("DELETE");
      conn.setRequestProperty("Authorization", "Bearer " + authToken);
      conn.connect();

      int responseCode = conn.getResponseCode();
      conn.disconnect();
      return responseCode == HttpURLConnection.HTTP_OK;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
}
