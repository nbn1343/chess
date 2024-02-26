package server;

import dataAccess.DataAccessException;
import dataAccess.MemoryUserDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryAuthDAO;
import model.UserData;
import model.AuthData;
import spark.*;
import com.google.gson.Gson;
import service.*;

import java.util.HashSet;

public class Server {

    private final MemoryUserDAO memoryUserDAO = new MemoryUserDAO ();
    private final MemoryGameDAO memoryGameDAO = new MemoryGameDAO ();
    private final MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO ();

    UserService userService = new UserService (memoryUserDAO, memoryAuthDAO);


    public int run (int desiredPort) {
        Spark.port (desiredPort);

        Spark.staticFiles.location ("web");

        // Register your endpoints and handle exceptions here.
        Spark.get ("/hello", (req, res) -> "Hello Nathan!");
        Spark.post ("/user", this :: register);
        Spark.post ("/session",this::login);
        Spark.delete("/session",this::logout);
//        Spark.get("/game",this::listGames);
//        Spark.post("/game",this::createGame);
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
                res.status (403); // Username already taken
            } else if (errorMessage.contains ("bad request")) {
                res.status (400); // Bad request
            } else {
                res.status (500); // Other errors
            }
            res.type ("application/json");
            return new Gson ().toJson (new ErrorMessage (errorMessage));
        } catch (Exception e) {
            res.status (500); // Internal Server Error
            res.type ("application/json");
            return new Gson ().toJson (new ErrorMessage ("Internal Server Error"));
        }

    }

    private Object login(Request req, Response res) {
        try {
            // Parse request body to extract username and password
            UserData userData = new Gson().fromJson(req.body(), UserData.class);
            String username = userData.username();
            String password = userData.password();

            // Call UserService to perform login
            AuthData authData = userService.login(username, password);

            // Build success response
            res.status(200);
            res.type("application/json");
            return new Gson().toJson(authData);
        } catch (DataAccessException e) {
            res.status(401); // Unauthorized
            res.type("application/json");
            return new Gson().toJson(new ErrorMessage(e.getMessage()));
        } catch (Exception e) {
            res.status(500); // Internal Server Error
            res.type("application/json");
            return new Gson().toJson(new ErrorMessage("Internal Server Error"));
        }
    }

    private Object logout(Request req, Response res) {
        try {
            // Extract authToken from the request headers
            String authToken = req.headers("authorization");

            // Call UserService to perform logout
            userService.logout(authToken);

            // Build success response
            res.status(200);
            res.type("application/json");
            return "{}";
        } catch (DataAccessException e) {
            res.status(401); // Unauthorized
            res.type("application/json");
            return new Gson().toJson(new ErrorMessage(e.getMessage()));
        } catch (Exception e) {
            res.status(500); // Internal Server Error
            res.type("application/json");
            return new Gson().toJson(new ErrorMessage("Internal Server Error"));
        }
    }

}
