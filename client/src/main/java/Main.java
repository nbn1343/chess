import chess.*;
import com.google.gson.JsonObject;

import java.io.OutputStreamWriter;
import java.util.Scanner;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
  private static final String SERVER_URL = "http://localhost:8080";


  private static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
      displayWelcomeMessage();
      while (true) {
        String command = scanner.nextLine().trim().toLowerCase();
        switch (command) {
          case "help":
            displayHelp();
            break;
          case "quit":
            System.out.println("Exiting Chess client. Goodbye!");
            System.exit(0);
            break;
          case "login":
            login();
            break;
          case "register":
            register();
            break;
          default:
            System.out.println("Invalid command. Please try again.");
        }
      }
    }
  private static void displayWelcomeMessage() {
    System.out.println("Welcome to 240 Chess Client! Type help to get started.");
  }

  private static void displayHelp() {
    System.out.println("Available commands:");
    System.out.println("  - Help: Displays available commands.");
    System.out.println("  - Quit: Exits the program.");
    System.out.println("  - Login: Log in to the Chess server.");
    System.out.println("  - Register: Register a new account and log in.");
  }

  private static void login() {
    System.out.print("Enter username: ");
    String username = scanner.nextLine().trim();
    System.out.print("Enter password: ");
    String password = scanner.nextLine().trim();

    try {
      URL url = new URL(SERVER_URL + "/session");
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setDoOutput(true);

      String requestBody = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";

      conn.getOutputStream().write(requestBody.getBytes());

      BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String line;
      StringBuilder response = new StringBuilder();
      while ((line = reader.readLine()) != null) {
        response.append(line);
      }
      reader.close();

      System.out.println("Login successful.");
      // Handle response as needed

      conn.disconnect();
    } catch (Exception e) {
      System.out.println("Login failed. Error: " + e.getMessage());
    }
  }

  private static void register() {
    System.out.print("Enter username: ");
    String username = scanner.nextLine().trim();
    System.out.print("Enter password: ");
    String password = scanner.nextLine().trim();
    System.out.print("Enter email: ");
    String email = scanner.nextLine().trim();

    try {
      URL url = new URL(SERVER_URL + "/user");
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setDoOutput(true);

      // Create JSON object for registration data
      JsonObject registrationData = new JsonObject();
      registrationData.addProperty("username", username);
      registrationData.addProperty("password", password);
      registrationData.addProperty("email", email);

      // Write registration data to request body
      OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
      writer.write(registrationData.toString());
      writer.flush();

      // Read response
      BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String line;
      StringBuilder response = new StringBuilder();
      while ((line = reader.readLine()) != null) {
        response.append(line);
      }
      reader.close();

      // Handle response as needed
      System.out.println("Registration successful.");

      conn.disconnect();
    } catch (Exception e) {
      System.out.println("Registration failed. Error: " + e.getMessage());
    }
  }

}