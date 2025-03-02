# Chat room client


### Project structure

```plaintext
chat-room-client/
│── src/
│   │── client/
│   │   │── util/
│   │   │   └── IOUtil.java
│   │   │── Client.java
│   │   │── MessageReceiver.java
│   │   └── MessageSender.java
│   └── ClientApplicationRunner.java
└── README.md
```

` ClientApplicationRunner.java`: The entry point of the application. Establishes a connection to the server at `localhost:8080` using
a `Socket`, then creates and starts a `Client` instance.

#### **`client/util/`**

- `IOUtil.java`: A utility class that provides methods for handling user input. It uses a `Scanner` to read input from the console and 
offers a method to retrieve user input (`getClientInput()`) as well as a method to close the scanner (`closeClientInput()`) when the 
application is shutting down.

#### **`client/`**

- `Client.java`: The core class of the chat-room client application, responsible for managing the client's connection to the server. It 
establishes a socket connection, handles sending and receiving messages, and maintains the client's connection status:
   - Stores the Socket instance and monitors connection status.
   - Reads user input and sends it to the server while receiving messages from the server.
   - Initializes a new client by creating a username.
   - Uses separate threads for sending and receiving messages to ensure uninterrupted chat functionality.
   - Provides a synchronized method to properly close the client connection.


- `MessageReceiver.java, MessageSender.java`: These classes handle message communication between the client and the server. Both 
implement the `Runnable` interface, allowing them to run in separate threads. This enables the client to send and receive messages 
concurrently, ensuring smooth, asynchronous communication without blocking either operation:

   **MessageReceiver:**
   - Runs in a separate thread.
   - Reads messages from the server using a `DataInputStream`.
   - Displays the received messages from the server.
   - Handles server exit command; if the received message is `"//exit initialized"`, the client connection is closed.

   **MessageSender:**
   - Runs in a separate thread.
   - Reads messages from the client using `IOUtil.getClientInput()`.
   - Uses a `DataOutputStream` to send messages to the server.
   - Detects when the user enters `EXIT_COMMAND` (`"//exit"`), stops sending messages, and allows the `MessageReceiver` to handle the
   final response from the server.

The `MessageReceiver` is the class that closes the client connection becouse after ending the `MessageSender` thread, the client needs to 
wait for a confirmation message from the server before shutting down completely.

---