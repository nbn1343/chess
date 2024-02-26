package server;

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
//        Spark.get("/game",this::listGames);
        Spark.post ("/game", this :: createGame);
//        Spark.put("/game",this::joinGame);
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
            res.status(400);
            res.type("application/json");
            return new Gson().toJson(new ErrorMessage("Error: bad request"));
        } catch (Exception e) {
            res.status(500);
            res.type("application/json");
            return new Gson().toJson(new ErrorMessage("Internal Server Error"));
        }


        }
    }
