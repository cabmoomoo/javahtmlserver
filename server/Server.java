package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import model.HTTPRequestMethod;
import model.State;
import utils.Request;
import utils.Response;
import utils.ResponseFactory;
import utils.Servlet;

public class Server {
    private final int port;
    private ServerSocket serverSocket;
    private final Map<HTTPRequestMethod, Map<String, Servlet>> endpointMap = new HashMap<>();
    private final ExecutorService threadPool = Executors.newFixedThreadPool(10);
    private State state;

    /*
     * Init the server
     */
    public Server(int port) {
        this.port = port;
        this.state = new State();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> shutdown()));
    }

    public void addEndpoint(HTTPRequestMethod method, String path, Servlet servlet) {
        Map<String, Servlet> methodMap = this.endpointMap.get(method);
        if (methodMap == null) {
            this.endpointMap.put(method, new HashMap<>());
            methodMap = this.endpointMap.get(method);
        }
        methodMap.put(path, servlet);
    }

    public void addGetEndpoint(String path, Servlet servlet) {
        Map<String, Servlet> methodMap = this.endpointMap.get(HTTPRequestMethod.GET);
        if (methodMap == null) {
            this.endpointMap.put(HTTPRequestMethod.GET, new HashMap<>());
            methodMap = this.endpointMap.get(HTTPRequestMethod.GET);
        }
        methodMap.put(path, servlet);
    }

    public void addPostEndpoint(String path, Servlet servlet) {
        Map<String, Servlet> methodMap = this.endpointMap.get(HTTPRequestMethod.POST);
        if (methodMap == null) {
            this.endpointMap.put(HTTPRequestMethod.POST, new HashMap<>());
            methodMap = this.endpointMap.get(HTTPRequestMethod.POST);
        }
        methodMap.put(path, servlet);
    }

    /*
     * Manage the server
     */

    // TODO: Shutdown doesn't seem to work
    // The console is never logged and the server only sometimes shuts down when commanded
    public void shutdown() {
        System.out.println("Shutting down...");
        try {
            Thread.sleep(1000);
            this.threadPool.shutdown();
            this.serverSocket.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * Running the server
     */
    public void start() {
        try {
            this.serverSocket = new ServerSocket(this.port);
            System.out.println("Starting server on port: " + this.port);
            System.out.println(this.endpointMap.get(HTTPRequestMethod.GET).keySet());
            while (this.serverSocket.isBound()) {
                Socket client = serverSocket.accept();
                this.threadPool.execute(() -> handle(client));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handle(Socket client) {
        Request req;
        Response res;
        try {
            req = new Request(client.getInputStream());
            res = new Response(client.getOutputStream());
        } catch (IOException e) {
            System.out.println("IOException encountered. Terminating thread.");
            return;
        }
        try {
            Map<String, Servlet> methodMap = this.endpointMap.get(req.method);
            Servlet servlet = methodMap.get(req.path);
            servlet.service(req, res, this.state);
        } catch (NullPointerException e) {
            String body = "No servlet exists for the following request:\nMethod: " + req.method.toString() + "; Path: " + req.path;
            System.out.println(body);
            ResponseFactory.badRequest(res, body)
                .send();
            return; // Return is unnecessary right now, but could save some trouble in the future if we add post-response activities to handle()
        }
    }

}
