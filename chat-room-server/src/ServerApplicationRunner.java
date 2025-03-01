import server.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerApplicationRunner {

    private static final int PORT = 8080;
    private static final int MAX_THREADS = 10;

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(PORT);
             ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS)) {

            new Server(serverSocket, executor).start();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
