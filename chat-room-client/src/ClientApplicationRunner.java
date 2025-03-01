import client.Client;

import java.io.IOException;
import java.net.Socket;

public class ClientApplicationRunner {

    private static final String SERVER_IP_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) {

        try (Socket socket = new Socket(SERVER_IP_ADDRESS, SERVER_PORT)) {

            new Client(socket).startClient();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}