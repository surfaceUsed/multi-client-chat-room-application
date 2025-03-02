package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Server {

    private final ServerSocket serverSocket;
    private final ExecutorService executor;
    private final List<Session> clientSessions = new ArrayList<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public Server(ServerSocket serverSocket, ExecutorService executor) {
        this.serverSocket = serverSocket;
        this.executor = executor;
    }

    /**
     *
     * Starts the server and continuously listens for client connections. Each accepted client is assigned a
     * new session and handled in a separate thread.
     */
    public void start() {

        // TODO: create way to manually shut down server.
        while (true) {

            try {

                System.out.println(MessageGenerator.waitingForClientConnection());
                this.executor.submit(new Session(this, this.serverSocket.accept()));
                System.out.println(MessageGenerator.clientConnectedToServer());

            } catch (IOException e) {
                System.err.println("[ERROR] Problem establishing connection with client: " + e.getMessage());
            }
        }
    }

    /**
     *
     * Broadcasts a message to all connected clients. Uses a read lock to ensure thread-safety.
     */
    void broadcastMessage(String message) throws IOException {
        this.readLock.lock();
        try {
            for (Session userSession : this.clientSessions) {
                userSession.sendMessage(message);
            }
        } finally {
            this.readLock.unlock();
        }
    }

    /**
     *
     * Returns a list of all the active client usernames. Uses a read lock to ensure thread-safety.
     */
    String listActiveClientConnectionsByUserName() {
        this.readLock.lock();
        try {
            return MessageGenerator.getAllUserNames(this.clientSessions);
        } finally {
            this.readLock.unlock();
        }
    }

    /**
     *
     * Adds a client session to the clientSession list. Uses a write lock to ensure exclusive access while
     * modifying the list.
     */
    void addClientSession(Session userSession) {
        this.writeLock.lock();
        try {
            this.clientSessions.add(userSession);
        } finally {
            this.writeLock.unlock();
        }
    }

    /**
     *
     * Removes a client session from the clientSession list. Uses a write lock to ensure exclusive access while
     * modifying the list.
     */
    void removeClientSession(Session userSession) {
        this.writeLock.lock();
        try {
            this.clientSessions.remove(userSession);
        } finally {
            this.writeLock.unlock();
        }
    }
}