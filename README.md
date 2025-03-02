# Chat room application

---

### Overview

This repository contains two components of a simple chat room application:

1. **Chat Room Server** - A multi-client chat server that allows multiple users to connect and communicate in real-time.

2. **Chat Room Client** - A client application that connects to the server and allows users to send and receive messages.

--- 

### Features

**Server:**
- **Multi-Client Support** – Handles multiple clients concurrently using a thread pool. 
- **User Management** – Tracks connected users and allows clients to view active participants.
- **Broadcast Messaging** – Messages are sent in real-time to all connected clients.
- **Server Commands** – Clients can use commands (`"//menu"`, `"//list"`, `"//exit"`) to navigate the chat.
- **Thread-Safe Communication** – Utilizes locks to manage concurrent user sessions.

**Client:**
- **Simple Command-Line Interface** – Intuitive UI for sending and receiving messages.
- **Real-Time Chat** – Instantly sends and receives messages from the server. 
- **User Authentication** – Prompts for a unique username upon connection.
- **Command Support** – Use `"//menu"` for help, `"//list"` to see online users, and `"//exit"` to leave. 
- **Session Handling** – Graceful disconnection ensures a clean exit from the chat room.

---

### How it works

**chat-room-server:**
The **server** acts as the central hub for communication. When it starts, it begins listening for client connections on a specified port. 
Each time a client connects, the server assigns a separate thread to handle that connection, allowing multiple users to chat 
simultaneously.

When a client joins, they register with a username and can start sending messages. The server receives each message and broadcasts it 
to all connected clients. If a user disconnects, the server removes them from the active list and notifies others.

The server also processes special commands that allow clients to retrieve a list of active users (`"//list"`), access a command menu
(`"//menu"`), or gracefully exit the chat (`"//exit"`).

**chat-room-client:**
The client connects to the chat server by specifying the server's hostname and port. Upon connection, the user is prompted to choose 
a username, which is used to identify them in the chat.

Once inside the chat, messages can be sent and received in real-time. The client runs two separate threads:

- One listens for messages from the server and displays them.
- The other handles user input, allowing messages to be sent while still receiving updates from others.

Users can also enter commands to see a list of active users (`"//list"`), view available chat options (`"//menu"`), or exit the session 
(`"//exit"`). When a user leaves, they are properly disconnected, and the server is notified.

---

### Running the application

1. Clone the repository.
```bash
git clone https://github.com/surfaceUsed/multi-client-chat-room-application.git
```

2. Navigate the project directory.

`chat-room-server`:
```bash
cd multi-client-chat-room-application/chat-room-server
```

`chat-room-client`:
```bash
cd multi-client-chat-room-application/chat-room-client
```

3. Compile application.

`chat-room-server`:
```bash
javac -d out src/ServerApplicationRunner.java src/server/*.java
```

`chat-room-client`:
```bash
javac -d out src/ClientApplicationRunner.java src/client/util/IOUtil.java src/client/*.java
```

4. Running the application.

**Note:** The server application needs to be running before the client application.

`chat-room-server`:
```bash
java -cp out ServerApplicationRunner
```

`chat-room-client`:
```bash
java -cp out ClientApplicationRunner
```
