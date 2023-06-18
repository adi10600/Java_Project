import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {
    private Set<PrintWriter> clientWriters = new HashSet<>();

    public Server() {
        try {
            // Create a server socket and start listening for incoming connections
            ServerSocket serverSocket = new ServerSocket(8000);
            System.out.println("Chat Server started on port 8000.");

            while (true) {
                // Accept a new client connection
                Socket clientSocket = serverSocket.accept();
                // Start a new thread to handle communication with the client
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;
        private String username;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                // Set up input and output streams for the client
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                // Get the username from the client
                username = in.readLine();
                System.out.println("New client connected: " + username);

                // Add the client's output stream to the set of client writers
                clientWriters.add(out);
                // Broadcast a message to all clients that the user has joined the chat
                broadcastMessage(username + " has joined the chat.");

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("/private")) {
                        // Handle private messages
                        sendPrivateMessage(message);
                    } else {
                        // Broadcast the message to all connected clients
                        broadcastMessage(username + ": " + message);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                // When the client disconnects, remove its output stream from the set of client writers
                if (username != null) {
                    clientWriters.remove(out);
                    // Broadcast a message to all clients that the user has left the chat
                    broadcastMessage(username + " has left the chat.");
                }
                try {
                    clientSocket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        private void broadcastMessage(String message) {
            // Send the message to all connected clients
            for (PrintWriter writer : clientWriters) {
                writer.println(message);
            }
        }

        // Method to handle private messages
        private void sendPrivateMessage(String message) {
            // Extract the recipient and private message from the command
            String[] parts = message.split(" ", 3);
            if (parts.length == 3) {
                String recipient = parts[1];
                String privateMsg = parts[2];
                // Send the private message to the intended recipient
                for (PrintWriter writer : clientWriters) {
                    if (writer != out && recipient.equals(username)) {
                        writer.println("[Private from " + username + "]: " + privateMsg);
                    } else if (writer == out && recipient.equals(username)) {
                        writer.println("[Private to " + recipient + "]: " + privateMsg);
                    }
                }
            }
        }

        // Method to handle disconnection of a client
        private void disconnect() {
            try {
                // Close the socket and input/output streams
                clientSocket.close();
                in.close();
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
