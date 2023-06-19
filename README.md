# Java_Project: Chat Application
This is a simple chat application built using Java Swing, allowing users to communicate with each other in real-time.
The name we have provided as "Chatter App"
Certainly! Here's an updated version of the implementation detail section, providing a step-by-step guide for running the chat application:

## Implementation Detail
The chat application is implemented using Java and consists of two main components: the client and the server.

### Client
1. The client component of the chat application is responsible for creating the graphical user interface (GUI) and handling user interactions.
2. The GUI is built using Java Swing, a GUI toolkit that provides components such as `JFrame`, `JEditorPane`, `JTextField`, `JButton`, and `JComboBox` for building the application window and capturing user input.
3. The `JFrame` class is used as the main window container, while the `JEditorPane` component is used to display the chat log. The chat log is rendered as HTML content to support formatting and display timestamps for each message.
4. The client GUI also includes a `JTextField` for entering messages, a `JButton` for sending messages, and a `JComboBox` for selecting emojis.
5. The client establishes a connection to the server using the `Socket` class, which allows for bi-directional communication over TCP/IP. The client sends and receives messages to and from the server using a `BufferedReader` for reading from the server and a `PrintWriter` for sending messages.

### Server
1. The server component of the chat application handles multiple client connections and manages the message broadcasting and private messaging functionality.
2. The server is implemented using the `ServerSocket` class, which listens for incoming client connections.
3. When a client connects, the server creates a new `ClientHandler` thread to handle communication with that client. The `ClientHandler` class extends the `Thread` class and is responsible for reading messages from the client and sending them to other clients.
4. The server maintains a list of connected clients using an `ArrayList`. This list is used to keep track of connected clients and send messages to all clients when a message is received from a client.
5. Private messages are handled by extracting the recipient's username from the message and finding the corresponding `ClientHandler` object from the list of clients. If the recipient is found, the server sends the private message only to that client.

### Communication Protocol
1. The chat application uses a simple text-based protocol for communication between the client and the server.
2. Clients send messages as plain text strings to the server, which processes and broadcasts them to all connected clients.
3. Private messages are indicated by starting the message with the "@" symbol, followed by the recipient's username and the actual message content.

### How it Works
1. Launch the Chat Application by running the `Client` class.
2. Enter a username when prompted by the application.
3. The application will connect to the server (running locally by default) using a Socket connection.
4. Once connected, the main chat window will appear.
5. Type your message in the message field and press Enter or click the Send button to send the message.
6. The message will be displayed in the chat log along with a timestamp.
7. If you select an emoji from the dropdown list, it will be appended to your message before sending.
8. Messages from other users connected to the server will be displayed in the chat log as well.
9. Private messages sent to you will be highlighted in blue.
10. The chat log will automatically scroll to show the latest messages.
11. To exit the application, simply close the main window.

### Requirements
- Java Development Kit (JDK) 8 or higher
- Git (optional)

### Running the Application
1. Clone the repository or download the source code.
2. Open the project in your preferred Java development environment.
3. Compile and run the `Client` class.

For Chatting between two person, Follow below step: -
javac Server.java on a terminal 
java Server.java 

On separate terminal: -
javac Client.java
java Client

On separate terminal 
java Client

Same as you can do multi-person chatting and personal chatting by using @ followed with username


### Customization

- To change the server address or port, modify the `connectToServer()` method in the `Client` class.
- To add or remove emojis from the dropdown list, modify the `emojis` array in the `Client` class.

### Compatibility

The chat application is compatible with any platform that supports Java Swing.

### Contributing

Contributions are welcome! If you find any issues or have suggestions for improvements, please create an issue or submit a pull request.

### License

The chat application is open source and released under the [MIT License](LICENSE). Feel free to use and modify the code according to your needs.

That's it! You now have a basic understanding of how the chat application works and how to run it. Feel free to customize and extend it further based on your requirements. Enjoy chatting!
