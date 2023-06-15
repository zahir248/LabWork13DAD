// Exercise 3

package demotcpserverdate;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.net.*;

public class Server extends JFrame {
    private JLabel statusLabel;

    public Server() {
        setTitle("Text Processing Server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(300, 150);

        // Create components
        statusLabel = new JLabel("Server is not running");

        // Add components to the layout
        add(statusLabel, BorderLayout.CENTER);
    }

    public void startServer(int port) {
        statusLabel.setText("Server is running on port " + port);

        try {
            ServerSocket serverSocket = new ServerSocket(port);

            while (true) {
                Socket clientSocket = serverSocket.accept();

                // Receive text from the client
                InputStream inputStream = clientSocket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String text = reader.readLine();

                // Count the number of words
                int wordCount = text.split("\\s+").length;

                // Send the word count back to the client
                OutputStream outputStream = clientSocket.getOutputStream();
                PrintWriter writer = new PrintWriter(outputStream, true);
                writer.println(wordCount);

                // Close the connection
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Server server = new Server();
                server.setVisible(true);
                
                int serverPort = 1234; // Specify the server port here
                server.startServer(serverPort);
            }
        });
    }
}


