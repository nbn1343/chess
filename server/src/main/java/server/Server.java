package server;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dataAccess.*;
import model.GameData;
import model.UserData;
import model.AuthData;
import spark.*;
import com.google.gson.Gson;
import service.*;

import java.io.Console;
import java.sql.SQLOutput;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

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
            memoryAuthDAO.getAllAuthData ();
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
            int gameID = gameData.gameID ();

            GameData createdGameID = gameService.createGame(gameID,whiteUsername, blackUsername, gameName, game, authToken);

            res.status(200);
            res.type("application/json");
            return new Gson ().toJson (createdGameID);
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
            String authToken = req.headers("authorization");

            if (!userService.isValidAuthToken(authToken)) {
                res.status(401); // Unauthorized
                res.type("application/json");
                return new Gson().toJson(new ErrorMessage("Error: authToken is invalid or expired"));
            }

            Collection<GameData> games = gameService.listGames (authToken);


            JsonObject jsonResponse = new JsonObject();
            jsonResponse.add("games", new Gson().toJsonTree(games));

            res.status(200);
            res.type("application/json");
            System.out.println (jsonResponse);
            return jsonResponse;
        } catch (Exception e) {
            res.status(500);
            return new Gson().toJson(new ErrorMessage("Error: " + e.getMessage()));
        }
    }

    private Object joinGame(Request req, Response res) {
        try {
            String authToken = req.headers("Authorization");
            if (authToken == null || authToken.isEmpty()) {
                res.status(401);
                return new Gson().toJson(new ErrorMessage("Error: unauthorized"));
            }
            if (!userService.isValidAuthToken(authToken)) {
                res.status(401);
                res.type("application/json");
                return new Gson().toJson(new ErrorMessage("Error: authToken is invalid or expired"));
            }

            JsonObject requestBody = new JsonParser().parse(req.body()).getAsJsonObject();
            int gameID = requestBody.get("gameID").getAsInt();
            String playerColor = null;
            if (requestBody.has("playerColor")) {
                playerColor = requestBody.get("playerColor").getAsString();
            }

            if (playerColor == null || playerColor.isEmpty()) {
                gameService.joinGame(authToken, gameID, null); // Pass null to indicate joining as an observer
            } else {
                gameService.joinGame(authToken, gameID, playerColor);
            }

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

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass () != o.getClass ()) return false;
        Server server = (Server) o;
        return Objects.equals (memoryUserDAO, server.memoryUserDAO) && Objects.equals (memoryGameDAO, server.memoryGameDAO) && Objects.equals (memoryAuthDAO, server.memoryAuthDAO) && Objects.equals (userService, server.userService) && Objects.equals (gameService, server.gameService);
    }

    @Override
    public int hashCode () {
        return Objects.hash (memoryUserDAO, memoryGameDAO, memoryAuthDAO, userService, gameService);
    }
}
