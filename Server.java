// Server
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private List<ClientHandler> clients;

    public Server(int port) {
        clients = new ArrayList<>();

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

                // Create a new client handler for each connected client
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    private class ClientHandler extends Thread {
        private Socket clientSocket;
        private BufferedReader reader;
        private PrintWriter writer;
        private String username;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(clientSocket.getOutputStream(), true);

                // Read the username sent by the client
                username = reader.readLine();

                // Notify all clients about the new user
                broadcastMessage(username + " has joined the chat.", null);

                String message;
                while ((message = reader.readLine()) != null) {
                    if (message.startsWith("@")) {
                        // Extract the recipient's username from the message
                        String recipient = message.substring(1, message.indexOf(' '));
                        String privateMessage = message.substring(message.indexOf(' ') + 1);

                        // Find the recipient's client handler
                        ClientHandler recipientHandler = findClientHandler(recipient);

                        if (recipientHandler != null) {
                            // Send the private message to the recipient
                            recipientHandler.sendMessage(username + " (Private): " + privateMessage);
                            // Send a copy of the private message to the sender
                            // sendMessage("To " + recipient + " (Private): " + privateMessage);
                        } else {
                            sendMessage("User " + recipient + " is not online.");
                        }
                    } else {
                        // Broadcast the message to all clients
                        broadcastMessage(username + ": " + message, this);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                clients.remove(this);

                // Notify all clients about the user leaving
                broadcastMessage(username + " has left the chat.", null);

                try {
                    reader.close();
                    writer.close();
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void sendMessage(String message) {
            writer.println(message);
        }

        private ClientHandler findClientHandler(String username) {
            for (ClientHandler client : clients) {
                if (client.username.equals(username)) {
                    return client;
                }
            }
            return null;
        }
    }

    public static void main(String[] args) {
        Server server = new Server(12345);
    }
}
