package client;

import java.io.DataInputStream;
import java.io.IOException;

public class MessageReceiver implements Runnable {

    private final Client client;
    private final DataInputStream input;

    MessageReceiver(Client client, DataInputStream input) {
        this.client = client;
        this.input = input;
    }

    @Override
    public void run() {

        String messageFromServer;

        while (this.client.isConnectionRunning()) {

            try {

                messageFromServer = this.client.receiveMessage(this.input);
                System.out.println(messageFromServer);
                if (messageFromServer.equals(Client.EXIT_COMMAND + " initialized")) {
                    this.client.closeClientConnection();
                    break;
                }

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
