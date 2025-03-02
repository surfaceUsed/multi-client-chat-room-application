package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class Session implements Runnable {

    private final Server server;
    private final Socket socket;
    private final DataInputStream input;
    private final DataOutputStream output;

    private String clientUserName;
    private boolean isTerminateSession = false;

    Session(Server server, Socket socket) throws IOException {
       this.server = server;
       this.socket = socket;
       this.input = new DataInputStream(this.socket.getInputStream());
       this.output = new DataOutputStream(this.socket.getOutputStream());
   }

    /**
     *
     * Handles the initial setup of a new client session. Prompts the client to enter a username and notifies all
     * users of their arrival.
     *
     * Returns true if the initialization process was successfully, false if not.
     */
   private boolean initializeNewClientSession() {
       try {

           sendMessage(MessageGenerator.createNewUserName());
           this.clientUserName = receiveMessage();
           this.server.addClientSession(this);
           sendMessage(MessageGenerator.welcomeMessage(this.clientUserName));
           sendMessage(MessageGenerator.notifyClientAboutCommandInputs());
           this.server.broadcastMessage(MessageGenerator.broadcastNewUserInChatRoom(this.clientUserName));
           return true;

       } catch (IOException ignored) {}

       return false;
   }

   void sendMessage(String message) throws IOException {
       this.output.writeUTF(message);
    }

    private String receiveMessage() throws IOException {
        return this.input.readUTF();
    }

    String getClientUserName() {
        return this.clientUserName;
    }

    @Override
    public void run() {

        if (initializeNewClientSession()) {

            try {

                String messageFromClient;
                while (!this.isTerminateSession) {
                    messageFromClient = receiveMessage();
                    handleMessageFromClient(messageFromClient);
                }

            } catch (IOException e) {
                System.err.println("[ERROR] Something went wrong: " + e.getMessage());

            } finally {

                this.server.removeClientSession(this);
                System.out.println(MessageGenerator.clientSessionEnded(this.clientUserName));
                terminateSession();
            }

        } else {
            System.err.println(MessageGenerator.creatingNewUserError());
            terminateSession();
        }
    }

    /**
     *
     * Handles messages received from the client.
     * Recognizes special commands (`//menu`, `//list`, `//exit`) and broadcasts normal messages.
     */
    private void handleMessageFromClient(String messageFromClient) throws IOException {

        switch (messageFromClient) {

            case MessageGenerator.MENU_COMMAND:
                sendMessage(MessageGenerator.menuOptions());
                break;

            case MessageGenerator.LIST_COMMAND:
                sendMessage(this.server.listActiveClientConnectionsByUserName());
                break;

            case MessageGenerator.EXIT_COMMAND:
                this.isTerminateSession = true;
                sendMessage(MessageGenerator.confirmingShutDownCommandFromClient());
                this.server.broadcastMessage(MessageGenerator.broadcastUserLeftChatRoom(this.clientUserName));
                break;

            default:
                this.server.broadcastMessage(this.clientUserName + ": " + messageFromClient);
                System.out.println(MessageGenerator.clientSentMessageConfirmation(this.clientUserName));
                break;
        }
    }

    private void terminateSession() {

       try {

           this.socket.close();
           this.input.close();
           this.output.close();

       } catch (IOException e) {
           System.err.println("Error closing session: " + e.getMessage());
       }
    }
}
