package server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dataAccess.*;
import model.GameData;
import model.UserData;
import model.AuthData;
import spark.*;
import com.google.gson.Gson;
import service.*;

import java.util.Collection;


public class Server {

    private final MemoryUserSQL memoryUserSQL;

    {
        try {
            memoryUserSQL = new MemoryUserSQL();
        } catch (DataAccessException e) {
            throw new RuntimeException (e);
        }
    }

    private final MemoryGameSQL memoryGameSQL;

    {
        try {
            memoryGameSQL = new MemoryGameSQL();
        } catch (DataAccessException e) {
            throw new RuntimeException (e);
        }
    }

    private final MemoryAuthSQL memoryAuthSQL;

    {
        try {
            memoryAuthSQL = new MemoryAuthSQL();
        } catch (DataAccessException e) {
            throw new RuntimeException (e);
        }
    }

    UserService userService = new UserService(memoryUserSQL, memoryAuthSQL);

    GameService gameService = new GameService(memoryGameSQL, memoryAuthSQL);


    public int run (int desiredPort) {
        Spark.port (desiredPort);

        Spark.staticFiles.location ("web");

        // Register your endpoints and handle exceptions here.
        Spark.get ("/hello", (req, res) -> "Hello Nathan!");
        Spark.post ("/user", this :: registerHandler);
        Spark.post ("/session", this :: loginHandler);
        Spark.delete ("/session", this :: logoutHandler);
        Spark.get("/game",this::listGamesHandler);
        Spark.post ("/game", this :: createGameHandler);
        Spark.put("/game",this::joinGameHandler);
        Spark.delete ("/db", (req, res) -> {
            memoryUserSQL.clear ();
            memoryGameSQL.clear ();
            memoryAuthSQL.clear ();
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

    private Object registerHandler (Request req, Response res) {
        try {
            UserData userData = new Gson ().fromJson (req.body (), UserData.class);
            String username = userData.username ();
            String password = userData.password ();
            String email = userData.email ();

            AuthData authData = userService.register (username, password, email);

            res.status (200);
            res.type ("application/json");
            memoryAuthSQL.getAllAuthData ();
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

    private Object loginHandler (Request req, Response res) {
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

    private Object logoutHandler (Request req, Response res) {
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

    private Object createGameHandler (Request req, Response res) {
        try {
            String authToken = req.headers("Authorization");

            if (authToken == null || authToken.isEmpty()) {
                res.status(401); // Unauthorized
                res.type("application/json");
                return new Gson().toJson(new ErrorMessage("Error: unauthorized"));
            }

            if (userService.isValidAuthToken (authToken)) {
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

    private Object listGamesHandler(Request req, Response res) {
        try {
            String authToken = req.headers("authorization");

            if (userService.isValidAuthToken (authToken)) {
                res.status(401); // Unauthorized
                res.type("application/json");
                return new Gson().toJson(new ErrorMessage("Error: authToken is invalid or expired"));
            }

            Collection<GameData> games = gameService.listGames (authToken);


            JsonObject jsonResponse = new JsonObject();
            jsonResponse.add("games", new Gson().toJsonTree(games));

            res.status(200);
            res.type("application/json");
            return jsonResponse;
        } catch (Exception e) {
            res.status(500);
            return new Gson().toJson(new ErrorMessage("Error: " + e.getMessage()));
        }
    }

    private Object joinGameHandler(Request req, Response res) {
        try {
            String authToken = req.headers("Authorization");
            if (authToken == null || authToken.isEmpty()) {
                res.status(401);
                return new Gson().toJson(new ErrorMessage("Error: unauthorized"));
            }
            if (userService.isValidAuthToken (authToken)) {
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
                gameService.joinGame(authToken, gameID, null);
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

}
