# Chat room server

### Project structure

```plaintext
chat-room-server/
│── src/
│   ├── server/
│   │   ├── MessageGenerator.java
│   │   ├── Server.java
│   │   └── Session.java
│   └── ServerApplicationRunner.java
└── README.md
```
- `ServerApplicationRunner.java`: The entry point of the application; initializes a `ServerSocket` on at `loaclhost:8080` and creates a 
fixed thread pool of maximum 10 threads to handle client connextions concurrently. It thens starts a `Server` instance. 

#### **`server/`**

- `MessageGenerator.java`: A utility class responsible for generating and managing server messages in the chat room. It centralizes all
predefines server messages like command responses, user notifications and system messages.

- `Server.java`: The class is responsible for managing client connections and facilitating communication in a chat room. It listens to
incoming client connections using a `ServerSocket` and handles multiple client sessions concurrently with an `ExecutorService`:
   - Assigns each client connection to a separate `Session` running in its own thread.
   - Broadcasts messages to all active client sessions.
   - Provides a `List` of all active client usernames.
   - Adds and removes client sessions in a thread-safe manner.

- `Session.java`: The class represents an individual client session in a chat server. Each client is assigned a separate `Session` 
instance, which runs in its own thread (`Runnable`). The session handles communication between the server and the client, processing
 messages and managing client interactions: 
    - Initializes a single client connection by asking the user for a username and sending a welcome message.
    - Responsible for reading and writing messages through `DataInputStream` and `DataOutputStream`. 
    - Processes specific client commands:
       - `//menu`: displays the chat room options menu.
       - `//list`: displays all the active users in the chat room.
       - `//exit`: signals that the user is leaving the chat room; initiates the closing of the client connection.
    - Interacts with the `Server` to add/remove client sessions, and facilitate message broadcasting.
    - Terminates the session upon client disconnection. 

    ---