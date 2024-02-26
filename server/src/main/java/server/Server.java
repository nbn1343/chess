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
        Spark.get("/hello", (req, res) -> "Hello Nathan!");
        Spark.post ("/user",this::register);
//        Spark.post ("/session",this::login);
//        Spark.delete("/session",this::logout);
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

    private Object register(Request req, Response res) {
        try {
            UserData userData = new Gson().fromJson(req.body(), UserData.class);
            String username = userData.username();
            String password = userData.password();
            String email = userData.email();

            AuthData authData = userService.register(username, password, email);

            res.status(200);
            res.type("application/json");
            return new Gson().toJson(authData);
        } catch (DataAccessException e) {
            res.status(403);
            res.type("application/json");
            return new Gson().toJson(new ErrorMessage(e.getMessage()));
        } catch (Exception e) {
            res.status(500);
            res.type("application/json");
            return new Gson().toJson(new ErrorMessage("Internal Server Error"));
        }
    }

}
