package ui;

import model.AuthData;
import java.util.Scanner;

public class ServerCommand {
  private static final int SERVER_URL = 8080;
  private static boolean loggedIn = false;
  private static final Scanner scanner = new Scanner(System.in);
  private static final ServerFacade serverFacade = new ServerFacade(SERVER_URL);

  public static void start() {
    while (true) {
      if (!loggedIn) {
        displayWelcomeMessage ();
        String command = scanner.nextLine ().trim ().toLowerCase ();
        switch (command) {
          case "help":
            displayHelp ();
            break;
          case "login":
            login ();
            break;
          case "register":
            register ();
            break;
          case "quit":
            System.out.println ("Exiting Chess client. Goodbye!");
            System.exit (0);
            break;
          default:
            System.out.println ("Invalid command. Please try again.");
        }
      } else {
        displayPostLoginOptions ();
        String command = scanner.nextLine ().trim ().toLowerCase ();
        switch (command) {
          case "help":
            displayHelp ();
            break;
          case "logout":
            logout();
            break;
          case "create game":
            System.out.print("Enter game name: ");
            String gameName = scanner.nextLine().trim();
            createGame(gameName);
            break;
          case "list games":
            listGames();
            break;
          case "join game":
            System.out.print("Enter game number: ");
            int gameNumber = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter desired color (white or black): ");
            String color = scanner.nextLine().trim().toLowerCase();
            if (color.equals("white") || color.equals("black")) {
              joinGame(gameNumber, color);
            } else {
              System.out.println("Invalid color. Please enter 'white' or 'black'.");
            }
            break;
          case "watch game":
            System.out.print("Enter game name: ");
            gameName = scanner.nextLine().trim();
            joinGameObserver(gameName);
            // Handle joining as an observer
            break;
          default:
            System.out.println("Invalid command. Please try again.");
        }
      }
    }
  }

  private static void displayWelcomeMessage() {
    System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + EscapeSequences.WHITE_QUEEN + "Welcome to 240 Chess! Type help to get started." + EscapeSequences.WHITE_QUEEN);
    System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE + "Enter Command: ");
  }

  private static void displayHelp() {
    System.out.println("Available commands:");
    System.out.println("  - Help: Displays available commands.");
    System.out.println("  - Login: Log in to the Chess server.");
    System.out.println("  - Register: Register a new account and log in.");
    System.out.println("  - Quit: Exits the program.");
  }

  private static void displayPostLoginOptions() {
    System.out.println("Postlogin Commands:");
    System.out.println("  - Help");
    System.out.println("  - Logout");
    System.out.println("  - Create Game");
    System.out.println("  - List Games");
    System.out.println("  - Join Game");
    System.out.println("  - Watch Game");
    System.out.println("  - Quit");
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
    boolean logoutSuccess = serverFacade.logout();
    if (logoutSuccess) {
      loggedIn = false;
      System.out.println("Logout successful.");
    } else {
      System.out.println("Logout failed.");
    }
  }

  private static void createGame(String gameName) {
    // Call the corresponding method in ServerFacade
    boolean createSuccess = serverFacade.createGame(gameName);
    if (createSuccess) {
      System.out.println("Game created successfully.");
    } else {
      System.out.println("Failed to create the game.");
    }
  }

  private static void listGames() {
    // Call the corresponding method in ServerFacade
    String gamesList = serverFacade.listGames();
    System.out.println("Games available:\n" + gamesList);
  }

  private static void joinGame(int gameNumber, String gameColor) {
    // Call the corresponding method in ServerFacade
    boolean joinSuccess = serverFacade.joinGame(gameNumber,gameColor);
    if (joinSuccess) {
      System.out.println("Joined the game successfully.");
    } else {
      System.out.println("Failed to join the game.");
    }
  }

  private static void joinGameObserver(String gameName) {
    // Call the corresponding method in ServerFacade
    boolean joinSuccess = serverFacade.joinGameObserver(gameName);
    if (joinSuccess) {
      System.out.println("Joined the game as observer successfully.");
    } else {
      System.out.println("Failed to join the game as observer.");
    }
  }
}
