package ui;

import model.AuthData;
import java.util.Scanner;

public class ServerCommand {
  private static final String SERVER_URL = "http://localhost:8080";
  private static boolean loggedIn = false;
  private static Scanner scanner = new Scanner(System.in);
  private static ServerFacade serverFacade = new ServerFacade(SERVER_URL);

  public static void start() {
    while (true) {
      if (!loggedIn) {
        displayWelcomeMessage();
      } else {
        displayPostLoginOptions();
      }
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
        case "logout":
          logout();
          break;
        default:
          System.out.println("Invalid command. Please try again.");
      }
    }
  }

  private static void displayWelcomeMessage() {
    System.out.println("Welcome to 240 Chess Client! Type help to get started.");
    System.out.print("Enter Command: ");
  }

  private static void displayHelp() {
    System.out.println("Available commands:");
    System.out.println("  - Help: Displays available commands.");
    System.out.println("  - Quit: Exits the program.");
    System.out.println("  - Login: Log in to the Chess server.");
    System.out.println("  - Register: Register a new account and log in.");
  }

  private static void displayPostLoginOptions() {
    System.out.println("Postlogin Commands:");
    System.out.println("  - Help");
    System.out.println("  - Quit");
    System.out.println("  - Logout");
    System.out.print("Enter command: ");
  }

  private static void login() {
    System.out.print("Enter username: ");
    String username = scanner.nextLine().trim();
    System.out.print("Enter password: ");
    String password = scanner.nextLine().trim();

    AuthData authData = serverFacade.login(username, password);

    if (authData != null) {
      loggedIn = true;
      System.out.println("Login successful.");
    } else {
      System.out.println("Login failed.");
    }
  }

  private static void register() {
    System.out.print("Enter username: ");
    String username = scanner.nextLine().trim();
    System.out.print("Enter password: ");
    String password = scanner.nextLine().trim();
    System.out.print("Enter email: ");
    String email = scanner.nextLine().trim();
    AuthData authData = serverFacade.register(username, password, email);

    if (authData != null) {
      System.out.println("Registration successful.");
    } else {
      System.out.println("Registration failed.");
    }
  }

  private static void logout() {
    if (serverFacade.logout()) {
      loggedIn = false;
      System.out.println("Logout successful.");
    } else {
      System.out.println("Logout failed.");
    }
  }
}
