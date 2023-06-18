import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Client extends JFrame {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private JTextArea chatLog;
    private JTextField messageField;
    private JButton sendButton;
    private String username;

    public Client(String serverAddress) {
        setTitle("Chat Client");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        chatLog = new JTextArea();
        chatLog.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatLog);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        messageField = new JTextField();
        inputPanel.add(messageField, BorderLayout.CENTER);

        sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);

        setVisible(true);

        try {
            // Establish connection with the server
            socket = new Socket(serverAddress, 8000);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Prompt the user to enter their username
            username = JOptionPane.showInputDialog(this, "Enter your username:");
            out.println(username);

            // Start a new thread to listen for incoming messages from the server
            new Thread(new IncomingReader()).start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void sendMessage() {
        // Get the message from the input field and send it to the server
        String message = messageField.getText();
        out.println(message);
        messageField.setText("");
    }

    private class IncomingReader implements Runnable {
        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    // Display the incoming message in the chat log
                    displayMessage(message);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void displayMessage(String message) {
        // Add the timestamp to the message and display it in the chat log
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String timestamp = sdf.format(new Date());

        chatLog.append("[" + timestamp + "] " + message + "\n");
    }

    // Method to handle private messages
    private void sendPrivateMessage(String recipient, String message) {
        // Send a private message to the server with the recipient's username and the message content
        out.println("/private " + recipient + " " + message);
    }

    // Method to handle disconnection from the server
    private void disconnect() {
        try {
            // Close the socket and input/output streams
            socket.close();
            in.close();
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Client("localhost"));
    }
}
