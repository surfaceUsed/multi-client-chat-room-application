package client;

import client.util.IOUtil;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {

    static final String EXIT_COMMAND = "//exit";

    private final Socket socket;

    private volatile boolean connectionIsRunningStatus = true;

    public Client(Socket socket) {
        this.socket = socket;
    }

    boolean isConnectionRunning() {
        return this.connectionIsRunningStatus;
    }

    synchronized void closeClientConnection() {
        this.connectionIsRunningStatus = false;
    }

    void sendMessage(DataOutputStream output, String message) throws IOException {
        output.writeUTF(message);
    }

    String receiveMessage(DataInputStream input) throws IOException {
        return input.readUTF();
    }

    public void startClient() {

        try (DataInputStream input = new DataInputStream(this.socket.getInputStream());
             DataOutputStream output = new DataOutputStream(this.socket.getOutputStream())) {

            if (initializeClientUsername(input, output)) {

                Thread receiver = new Thread(new MessageReceiver(this, input));
                Thread sender = new Thread(new MessageSender(this, output));

                receiver.start();
                sender.start();

                try {
                    sender.join();
                    receiver.join();
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }

            } else {
                System.out.println("Error initializing client.");
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            IOUtil.closeClientInput();
        }
    }

    private boolean initializeClientUsername(DataInputStream input, DataOutputStream output) {

        try {

            String fromServer = receiveMessage(input);
            System.out.println(fromServer);
            sendMessage(output, IOUtil.getClientInput());
            return true;

        } catch (IOException ignored) {}
        IOUtil.closeClientInput();
        return false;
    }
}