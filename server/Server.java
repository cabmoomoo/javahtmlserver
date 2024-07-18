package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import utils.Request;
import utils.Response;
import utils.Servlet;

public class Server {
    private int port;
    private ServerSocket serverSocket;
    private Map<String, Servlet> endpointMap = new HashMap<>();
    private ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public Server(int port) {
        this.port = port;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> shutdown()));
    }

    public void addEndpoint(String path, Servlet servlet) {
        this.endpointMap.put(path, servlet);
    }

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

    public void start() {
        try {
            this.serverSocket = new ServerSocket(this.port);
            System.out.println("Starting server on port: " + this.port);
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
        Servlet servlet = this.endpointMap.get(req.path);
        servlet.service(req, res);
    }

}
