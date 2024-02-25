package server;

import dataAccess.DataAccessException;
import dataAccess.MemoryUserDAO;
import model.UserData;
import spark.*;
import com.google.gson.Gson;

import java.util.HashSet;

public class Server {

    private MemoryUserDAO memoryUserDAO = new MemoryUserDAO ();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.get("/hello", (req, res) -> "Hello BYU!");
        Spark.delete("/db", (req, res) -> {memoryUserDAO.clear(); res.status(200); return "{}";});


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }


}


