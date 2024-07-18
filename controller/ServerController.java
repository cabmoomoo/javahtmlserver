package controller;

// import server.Server;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import model.HTTPContentType;
import model.HTTPStatus;
import server.Server;
import utils.Request;
import utils.Response;
import utils.Servlet;

public class ServerController {

    public Server startAPI(int port) {
        Server server = new Server(port);
        server.addEndpoint("/", this.endpointIndex);
        server.addEndpoint("/special", this.endpointSpecial);
        server.addEndpoint("/test", this.endpointTest);
        return server;
    }

    Servlet endpointIndex = (Request req, Response res) -> {
        File indexFile = new File("resources/index.html");
        try (Scanner reader = new Scanner(indexFile)) {
            String body = "";
            while (reader.hasNextLine()) {
                body += reader.nextLine();
            }
            res.send(body, HTTPStatus.OK, HTTPContentType.HTML);
        } catch (FileNotFoundException e) {
            // Mozilla notes that for an API, 404 can also mean "the endpoint is valid but the resource itself does not exist"
            // https://developer.mozilla.org/en-US/docs/Web/HTTP/Status#client_error_responses
            res.send("Could not find file", HTTPStatus.NOT_FOUND, HTTPContentType.HTML);
        }
    };

    Servlet endpointSpecial = (Request req, Response res) -> {
        File specialFile = new File("resources/special.html");
        try (Scanner reader = new Scanner(specialFile)) {
            String body = "";
            while (reader.hasNextLine()) {
                body += reader.nextLine();
            }
            res.send(body, HTTPStatus.OK, HTTPContentType.HTML);
        } catch (FileNotFoundException e) {
            res.send("Could not find file", HTTPStatus.NOT_FOUND, HTTPContentType.HTML);
        }
    };

    Servlet endpointTest = (Request req, Response res) -> {
        res.send("This is a plain-text test", HTTPStatus.OK, HTTPContentType.HTML);
    };
    
}