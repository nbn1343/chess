package server;

import dataAccess.DataAccessException;
import dataAccess.MemoryUserDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryAuthDAO;
import model.UserData;
import spark.*;
import com.google.gson.Gson;
import service.*;

import java.util.HashSet;

public class Server {

    private final MemoryUserDAO memoryUserDAO = new MemoryUserDAO ();
    private final MemoryGameDAO gameUserDAO = new MemoryGameDAO ();
    private final MemoryAuthDAO authUserDAO = new MemoryAuthDAO ();



    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
//        Spark.get("/hello", (req, res) -> "Hello BYU!");
        Spark.post ("/user",this::register);
//        Spark.post ("/session",this::login);
//        Spark.delete("/session",this::logout);
//        Spark.get("/game",this::listGames);
//        Spark.post("/game",this::createGame);
//        Spark.put("/game",this::joinGame);
        Spark.delete("/db", (req, res) -> {memoryUserDAO.clear(); gameUserDAO.clear(); authUserDAO.clear(); res.status(200); return "{}";});

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object register(Request req, Response res) throws DataAccessException {
        

    }
}



