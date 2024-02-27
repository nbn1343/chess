package server;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dataAccess.DataAccessException;
import dataAccess.MemoryUserDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryAuthDAO;
import model.GameData;
import model.UserData;
import model.AuthData;
import spark.*;
import com.google.gson.Gson;
import service.*;

import java.util.List;

public class Server {

    private final MemoryUserDAO memoryUserDAO = new MemoryUserDAO ();
    private final MemoryGameDAO memoryGameDAO = new MemoryGameDAO ();
    private final MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO ();

    UserService userService = new UserService (memoryUserDAO, memoryAuthDAO);

    GameService gameService = new GameService (memoryGameDAO, memoryAuthDAO);

    public int run (int desiredPort) {
        Spark.port (desiredPort);

        Spark.staticFiles.location ("web");

        // Register your endpoints and handle exceptions here.
        Spark.get ("/hello", (req, res) -> "Hello Nathan!");
        Spark.post ("/user", this :: register);
        Spark.post ("/session", this :: login);
        Spark.delete ("/session", this :: logout);
        Spark.get("/game",this::listGames);
        Spark.post ("/game", this :: createGame);
        Spark.put("/game",this::joinGame);
        Spark.delete ("/db", (req, res) -> {
            memoryUserDAO.clear ();
            memoryGameDAO.clear ();
            memoryAuthDAO.clear ();
            res.status (200);
            return "{}";
        });

        Spark.awaitInitialization ();
        return Spark.port ();
    }

    public void stop () {
        Spark.stop ();
        Spark.awaitStop ();
    }

    private Object register (Request req, Response res) {
        try {
            UserData userData = new Gson ().fromJson (req.body (), UserData.class);
            String username = userData.username ();
            String password = userData.password ();
            String email = userData.email ();

            AuthData authData = userService.register (username, password, email);

            res.status (200);
            res.type ("application/json");
            return new Gson ().toJson (authData);
        } catch (DataAccessException e) {
            String errorMessage = e.getMessage ();
            if (errorMessage.contains ("already taken")) {
                res.status (403);
            } else if (errorMessage.contains ("bad request")) {
                res.status (400);
            } else {
                res.status (500);
            }
            res.type ("application/json");
            return new Gson ().toJson (new ErrorMessage (errorMessage));
        } catch (Exception e) {
            res.status (500);
            res.type ("application/json");
            return new Gson ().toJson (new ErrorMessage ("Internal Server Error"));
        }

    }

    private Object login (Request req, Response res) {
        try {
            UserData userData = new Gson ().fromJson (req.body (), UserData.class);
            String username = userData.username ();
            String password = userData.password ();

            AuthData authData = userService.login (username, password);

            res.status (200);
            res.type ("application/json");
            return new Gson ().toJson (authData);
        } catch (DataAccessException e) {
            res.status (401); // Unauthorized
            res.type ("application/json");
            return new Gson ().toJson (new ErrorMessage (e.getMessage ()));
        } catch (Exception e) {
            res.status (500);
            res.type ("application/json");
            return new Gson ().toJson (new ErrorMessage ("Internal Server Error"));
        }
    }

    private Object logout (Request req, Response res) {
        try {
            String authToken = req.headers ("authorization");

            userService.logout (authToken);

            res.status (200);
            res.type ("application/json");
            return "{}";
        } catch (DataAccessException e) {
            res.status (401); // Unauthorized
            res.type ("application/json");
            return new Gson ().toJson (new ErrorMessage (e.getMessage ()));
        } catch (Exception e) {
            res.status (500);
            res.type ("application/json");
            return new Gson ().toJson (new ErrorMessage ("Internal Server Error"));
        }
    }

    private Object createGame (Request req, Response res) {
        try {
            String authToken = req.headers("Authorization");

            if (authToken == null || authToken.isEmpty()) {
                res.status(401); // Unauthorized
                res.type("application/json");
                return new Gson().toJson(new ErrorMessage("Error: unauthorized"));
            }

            if (!userService.isValidAuthToken(authToken)) {
                res.status(401); // Unauthorized
                res.type("application/json");
                return new Gson().toJson(new ErrorMessage("Error: authToken is invalid or expired"));
            }

            GameData gameData = new Gson().fromJson(req.body(), GameData.class);
            String gameName = gameData.gameName();
            String whiteUsername = gameData.whiteUsername();
            String blackUsername = gameData.blackUsername();
            Object game = gameData.game();

            int createdGameID = gameService.createGame(whiteUsername, blackUsername, gameName, game, authToken);

            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("gameID", createdGameID);
            res.status(200);
            res.type("application/json");
            return jsonResponse.toString();
        } catch (DataAccessException e) {
            String errorMessage = e.getMessage ();
            if (errorMessage.contains ("bad request")) {
                res.status (400);
            } else {
                res.status (500);
            }
            res.type ("application/json");
            return new Gson ().toJson (new ErrorMessage (errorMessage));
        } catch (Exception e) {
            res.status (500);
            res.type ("application/json");
            return new Gson ().toJson (new ErrorMessage ("Internal Server Error"));
        }


        }

    private Object listGames(Request req, Response res) {
        try {
            // Extract authToken from request headers
            String authToken = req.headers("authorization");

            // Check if authToken is valid
            if (!userService.isValidAuthToken(authToken)) {
                res.status(401); // Unauthorized
                res.type("application/json");
                return new Gson().toJson(new ErrorMessage("Error: authToken is invalid or expired"));
            }

            // Call GameService to get the list of games
            List<GameData> games = gameService.listGames(authToken);

            // Build JSON response
            JsonArray gamesArray = new JsonArray();
            for (GameData game : games) {
                JsonObject gameObject = new JsonObject();
                gameObject.addProperty("gameID", game.gameID());
                gameObject.addProperty("whiteUsername", game.whiteUsername());
                gameObject.addProperty("blackUsername", game.blackUsername());
                gameObject.addProperty("gameName", game.gameName());
                gamesArray.add(gameObject);
            }

            JsonObject jsonResponse = new JsonObject();
            jsonResponse.add("games", gamesArray);

            res.status(200); // OK
            res.type("application/json");
            return jsonResponse.toString();
        } catch (Exception e) {
            res.status(500); // Internal Server Error
            return new Gson().toJson(new ErrorMessage("Error: " + e.getMessage()));
        }
    }

    private Object joinGame(Request req, Response res) {
        try {
            // Extract authToken from the request headers
            String authToken = req.headers("Authorization");
            if (authToken == null || authToken.isEmpty()) {
                res.status(401); // Unauthorized
                return new Gson().toJson(new ErrorMessage("Error: unauthorized"));
            }
            if (!userService.isValidAuthToken(authToken)) {
                res.status(401); // Unauthorized
                res.type("application/json");
                return new Gson().toJson(new ErrorMessage("Error: authToken is invalid or expired"));
            }

            // Extract gameID and playerColor from the request body
            JsonObject requestBody = new JsonParser().parse(req.body()).getAsJsonObject();
            int gameID = requestBody.get("gameID").getAsInt();
            String playerColor = requestBody.get("playerColor").getAsString();

            // Call GameService to join the game
            gameService.joinGame(authToken, gameID, playerColor);

            // Build success response
            res.status(200);
            res.type("application/json");
            return "{}";
        } catch (DataAccessException e) {
            String errorMessage = e.getMessage ();
            if (errorMessage.contains ("already taken")) {
                res.status (403);
            } else if (errorMessage.contains ("bad request")) {
                res.status (400);
            } else {
                res.status (500);
            }
            res.type ("application/json");
            return new Gson ().toJson (new ErrorMessage (errorMessage));
        } catch (Exception e) {
            res.status (500);
            res.type ("application/json");
            return new Gson ().toJson (new ErrorMessage ("Internal Server Error"));
        }
    }


}
