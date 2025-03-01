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

    public void start() {

        // TODO: create way to manually shut down server.
        while (true) {

            try {

                System.out.println(MessageHandler.waitingForClientConnection());
                this.executor.submit(new Session(this, this.serverSocket.accept()));
                System.out.println(MessageHandler.clientConnectedToServer());

            } catch (IOException e) {
                System.err.println("[ERROR] Problem establishing connection with client: " + e.getMessage());
            }
        }
    }

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

    String listActiveClientConnectionsByUserName() {
        this.readLock.lock();
        try {
            return MessageHandler.getAllUserNames(this.clientSessions);
        } finally {
            this.readLock.unlock();
        }
    }

    void addClientSession(Session userSession) {
        this.writeLock.lock();
        try {
            this.clientSessions.add(userSession);
        } finally {
            this.writeLock.unlock();
        }
    }

    void removeClientSession(Session userSession) {
        this.writeLock.lock();
        try {
            this.clientSessions.remove(userSession);
        } finally {
            this.writeLock.unlock();
        }
    }
}