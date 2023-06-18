# Java_Project

Sure! Here's a short README for the chat application:

## Chat Application

This is a simple chat application built using Java Swing, allowing users to communicate with each other in real-time.
The name we have provided as "Chatter App"

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
