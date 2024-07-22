package controller;

// import server.Server;
import java.io.File;
import model.HTTPContentType;
import model.HTTPStatus;
import server.Server;
import service.LocalFileService;
import service.StateService;
import utils.Request;
import utils.Response;
import utils.ResponseFactory;
import utils.Servlet;

public class ServerController {

    private final LocalFileService localFileService;
    private final StateService stateService;

    public ServerController() {
        this.localFileService = new LocalFileService();
        this.stateService = new StateService();
    }

    public Server startAPI(int port) {
        Server server = new Server(port);
        server.addGetEndpoint("/", this::endpointIndex);
        server.addGetEndpoint("/special", this::endpointSpecial);
        server.addGetEndpoint("/state", this::endpointGetState);
        server.addPostEndpoint("/state/addOne", this::endpointAddOne);
        server.addPostEndpoint("/state/subOne", this::endpointSubOne);
        server.addPostEndpoint("/state/add", this::endpointStateAdd);
        server.addPostEndpoint("/state/sub", this::endpointStateSub);
        server.addGetEndpoint("/test", this.endpointTest);
        return server;
    }

    private void endpointIndex(Request req, Response res) {
        File indexFile = new File("resources/index.html");
        String body = this.localFileService.fileToString(indexFile);
        if (body == null) {
            // Mozilla notes that for an API, 404 can also mean "the endpoint is valid but the resource itself does not exist"
            // https://developer.mozilla.org/en-US/docs/Web/HTTP/Status#client_error_responses
            ResponseFactory.fileNotFoundResponse(res).send();
        }
        new ResponseFactory(res)
            .setBody(body)
            .setStatus(HTTPStatus.OK)
            .setType(HTTPContentType.HTML)
            .build()
            .send();
    };

    private void endpointSpecial(Request req, Response res) {
        File specialFile = new File("resources/special.html");
        String body = this.localFileService.fileToString(specialFile);
        if (body == null) {
            ResponseFactory.fileNotFoundResponse(res).send();
        }
        new ResponseFactory(res)
            .setBody(body)
            .setStatus(HTTPStatus.OK)
            .setType(HTTPContentType.HTML)
            .build()
            .send();
    };

    private void endpointGetState(Request req, Response res) {
        int value = this.stateService.getState();
        new ResponseFactory(res)
            .setStatus(HTTPStatus.OK)
            .setType(HTTPContentType.PLAIN)
            .setBody(String.valueOf(value))
            .build()
            .send();
    };

    private void endpointAddOne(Request req, Response res) {
        this.stateService.stateAdd(1);
        ResponseFactory.plainOK(res)
            .setBody("Successfully added 1 to state")
            .build()
            .send();
    };

    private void endpointSubOne(Request req, Response res) {
        this.stateService.stateSub(1);
        ResponseFactory.plainOK(res)
            .setBody("Successfully subtracted 1 from state")
            .build()
            .send();
    };

    private void endpointStateAdd(Request req, Response res) {
        int amt;
        try {
            amt = Integer.parseInt(req.body);
        } catch (NumberFormatException e) {
            ResponseFactory.badRequest(res, "Body could not be parsed into int").send();
            return;
        }
        this.stateService.stateAdd(amt);
        ResponseFactory.plainOK(res)
            .setBody(String.format("Successfully added %d to state", amt))
            .build()
            .send();
    }

    private void endpointStateSub(Request req, Response res) {
        int amt;
        try {
            amt = Integer.parseInt(req.body);
        } catch (NumberFormatException e) {
            ResponseFactory.badRequest(res, "Body could not be parsed into int").send();
            return;
        }
        this.stateService.stateSub(amt);
        ResponseFactory.plainOK(res)
            .setBody(String.format("Successfully subtracted %d from state", amt))
            .build()
            .send();
    }

    /**
     * This test is legacy in that...
     * <ul>
     *  <li>It is only a test, and would be removed before production</li>
     *  <li>It demonstrates the old method for send a response</li>
     *  <li>It demonstrates an alternative method for defining a Servlet endpoint</li>
     * </ul>
     * Fortunately, this is a practice repository, so we can enjoy leaving examples like this lying around
     */
    Servlet endpointTest = (Request req, Response res) -> {
        res.send("This is a plain-text test", HTTPStatus.OK, HTTPContentType.PLAIN);
    };
    
}