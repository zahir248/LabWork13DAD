// Exercise 3

package demotcpclientdate;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.*;

public class Client extends JFrame {
    private JTextArea textArea;
    private JTextField serverAddressField;
    private JTextField serverPortField;
    private JButton processButton;
    private JLabel wordCountLabel;

    public Client() {
        setTitle("Text Processing Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(400, 300);

        // Create components
        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        serverAddressField = new JTextField();
        serverPortField = new JTextField();
        processButton = new JButton("Process");
        wordCountLabel = new JLabel();

        // Add components to the layout
        JPanel topPanel = new JPanel(new GridLayout(2, 2));
        topPanel.add(new JLabel("Server Address:"));
        topPanel.add(serverAddressField);
        topPanel.add(new JLabel("Server Port:"));
        topPanel.add(serverPortField);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(processButton, BorderLayout.SOUTH);
        add(wordCountLabel, BorderLayout.EAST);

        // Add event listeners
        processButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                processText();
            }
        });
    }

    public void processText() {
        String text = textArea.getText();
        String serverAddress = serverAddressField.getText();
        int serverPort = Integer.parseInt(serverPortField.getText());

        try {
            Socket socket = new Socket(serverAddress, serverPort);

            // Send text to the server
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream, true);
            writer.println(text);

            // Receive word count from the server
            InputStream inputStream = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String countString = reader.readLine();
            int wordCount = Integer.parseInt(countString);

            // Display word count
            wordCountLabel.setText("Word Count: " + wordCount);

            // Close the connection
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Client client = new Client();
                client.setVisible(true);
            }
        });
    }
    
    // input
    // Server Address: localhost
    // Server Port: 1234
}
