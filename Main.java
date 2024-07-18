import controller.ServerController;
import server.Server;

public class Main {
    public static void main(String[] args) {
        ServerController serverController = new ServerController();
        int port = 8080;
        
        Server server = serverController.startAPI(port);
        server.start();
    }
}