// Client
import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
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
    private JEditorPane chatLog;
    private JTextField messageField;
    private JButton sendButton;
    private JComboBox<String> emojiDropdown;

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String username;

    private static final String[] emojis = {
        "😊", "😂", "😍", "👍", "👏", "🔥", "🎉", "🌟", "❤️", "✨",
        "😃", "😄", "😁", "😆", "😎", "😋", "😜", "🙌", "👌", "🌈",
        "👋", "🙏", "🎶", "🎈", "🎁", "💡", "💪", "💕", "💖", "💯",
        "🚀", "⭐", "💫", "🌼", "🌺", "🌞", "🍀", "🍕", "🍔", "🍦",
        "🍓", "🍎", "🍉", "🍩", "🍭", "🍺", "🎸", "🎮", "⚽", "🚲"
    };

    public Client() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(null);

        chatLog = new JEditorPane();
        chatLog.setEditable(false);
        chatLog.setContentType("text/html");
        chatLog.setPreferredSize(new Dimension(400, 250));
        JScrollPane scrollPane = new JScrollPane(chatLog);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        messageField = new JTextField(20);
        sendButton = new JButton("Send");
        emojiDropdown = new JComboBox<>(emojis);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(emojiDropdown, BorderLayout.WEST);
        inputPanel.add(sendButton, BorderLayout.EAST);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        messageField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        emojiDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedEmoji = (String) emojiDropdown.getSelectedItem();
                if (selectedEmoji != null && !selectedEmoji.isEmpty()) {
                    messageField.setText(messageField.getText() + selectedEmoji);
                    messageField.requestFocus();
                }
            }
        });
    }

    private void connectToServer(String username) {
        try {
            setTitle("Client["+username+"]                               Chatter App");
            this.username = username;
            socket = new Socket("localhost", 12345); // Replace "localhost" with the server IP address if running on different machines
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            writer.println(username);

            new Thread(new IncomingMessageHandler()).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            writer.println(message);
            messageField.setText("");
            appendMessage(message, true);
        }
    }

    private void appendMessage(String message, boolean isMainUser) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String timestamp = sdf.format(new Date());

        StringBuilder formattedMessage = new StringBuilder("<html><body>");

        if (isMainUser) {
            if (message.startsWith("To")) {
                formattedMessage.append("<div style='text-align: right; color: blue;'>");
                formattedMessage.append("[" + timestamp + "] <br>");
                formattedMessage.append(message);
            } else {
                formattedMessage.append("<div style='text-align: right; color: blue;'>");
                formattedMessage.append("[" + timestamp + "] <br>");
                formattedMessage.append("You: " + message);
            }
        } else {
            formattedMessage.append("<div style='text-align: left; color: green;'>");
            formattedMessage.append("[" + timestamp + "] <br>");
            formattedMessage.append(message);
        }

        formattedMessage.append("</div></body></html>");

        SwingUtilities.invokeLater(() -> {
            HTMLEditorKit editorKit = (HTMLEditorKit) chatLog.getEditorKit();
            HTMLDocument doc = (HTMLDocument) chatLog.getDocument();
            try {
                editorKit.insertHTML(doc, doc.getLength(), formattedMessage.toString(), 0, 0, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            chatLog.setCaretPosition(chatLog.getDocument().getLength());
        });
    }

    private class IncomingMessageHandler implements Runnable {
        @Override
        public void run() {
            try {
                String message;
                while ((message = reader.readLine()) != null) {
                    appendMessage(message, false);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Client clientGUI = new Client();
            clientGUI.setVisible(true);

            String username = JOptionPane.showInputDialog(clientGUI, "Enter your username:");
            if (username != null && !username.isEmpty()) {
                clientGUI.connectToServer(username);
            } else {
                JOptionPane.showMessageDialog(clientGUI, "Invalid username. Exiting the application.");
                System.exit(0);
            }
        });
    }
}
