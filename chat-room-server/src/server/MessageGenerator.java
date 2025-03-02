package server;

import java.util.List;

class MessageGenerator {

    private static final String SERVER_PREFIX = "[SERVER]";

    static final String MENU_COMMAND = "//menu";
    static final String LIST_COMMAND = "//list";
    static final String EXIT_COMMAND = "//exit";

    private MessageGenerator() {}

    static String menuOptions() {
        return String.format("""
            
            \t1. Menu: "%s"
            \t2. List online users: "%s"
            \t3. Exit chat room: "%s"
            """,
            MENU_COMMAND, LIST_COMMAND, EXIT_COMMAND);
    }

    static String waitingForClientConnection() {
        return "Waiting for client to connect to server...";
    }

    static String clientConnectedToServer() {
       return "Client connected!";
    }

    static String clientSentMessageConfirmation(String username) {
        return "\"" + username + "\" sent a message.";
    }

    static String clientSessionEnded(String username) {
        return  "\"" + username + "\" disconnected from the server.";
    }

    static String createNewUserName() {
        return SERVER_PREFIX + " Create a user name: ";
    }

    static String welcomeMessage(String username) {
        return SERVER_PREFIX + " Welcome \"" + username + "\" to the public chat room!";
    }

    static String notifyClientAboutCommandInputs() {
        return SERVER_PREFIX + " For list of default command inputs, type \"" + MENU_COMMAND + "\" and press enter.";
    }

    static String broadcastNewUserInChatRoom(String username) {
        return SERVER_PREFIX + " \"" + username + "\" entered the chat.";
    }

    static String confirmingShutDownCommandFromClient() {
        return EXIT_COMMAND + " initialized";
    }

    static String broadcastUserLeftChatRoom(String username) {
        return SERVER_PREFIX + " user \"" + username + "\" has left the chat room.";
    }

    static String getAllUserNames(List<Session> clientSessions) {
        StringBuilder sb = new StringBuilder();
        sb.append("Online users").append("(").append(clientSessions.size()).append("): ").append("\n");
        for (Session session : clientSessions) {
            sb.append(session.getClientUserName()).append("\n");
        }
        return sb.toString().trim();
    }

    static String creatingNewUserError() {
        return SERVER_PREFIX + " Creating new user led to an unexpected error. Connection closed.";
    }
}