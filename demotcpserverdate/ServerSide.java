// Exercise 6

package demotcpserverdate;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerSide extends JFrame {
    private static final int SERVER_PORT = 8080;

    private JTextArea logArea;

    public ServerSide() {
        setTitle("Text Translation Server");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create components
        logArea = new JTextArea();
        logArea.setEditable(false);

        // Create layout
        setLayout(new BorderLayout());
        add(new JScrollPane(logArea), BorderLayout.CENTER);

        // Start the server in a separate thread
        startServer();
    }

    private void startServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ServerSocket serverSocket = null;
                try {
                    // Create server socket
                    serverSocket = new ServerSocket(SERVER_PORT);
                    log("Server started on port " + SERVER_PORT);

                    while (true) {
                        // Accept client connection
                        Socket clientSocket = serverSocket.accept();
                        log("Client connected: " + clientSocket.getInetAddress());

                        // Handle client request in a separate thread
                        new Thread(new ClientHandler(clientSocket)).start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (serverSocket != null) {
                        try {
                            serverSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    private void log(String message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                logArea.append(message + "\n");
            }
        });
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                BufferedReader clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

                // Read the text and target language from the client
                String text = clientReader.readLine();
                String targetLanguage = clientReader.readLine();

                // Perform translation
                String translatedText = translateText(text, targetLanguage);

                // Send the translated text back to the client
                writer.println(translatedText);

                // Close the client connection
                clientSocket.close();
                log("Client disconnected: " + clientSocket.getInetAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String translateText(String text, String targetLanguage) {
            // Perform the translation based on the provided text and target language
            // You can implement your translation logic here
            // For simplicity, let's assume we have a map of translations

            Map<String, Map<String, String>> translations = new HashMap<>();
            Map<String, String> englishTranslations = new HashMap<>();
            englishTranslations.put("Bahasa Malaysia", "Selamat pagi");
            englishTranslations.put("Arabic", "الخير صباح");
            englishTranslations.put("Korean", "좋은 아침");
            translations.put("Good morning", englishTranslations);

            Map<String, String> malayTranslations = new HashMap<>();
            malayTranslations.put("English", "Goodbye");
            malayTranslations.put("Arabic", "مع السلامة");
            malayTranslations.put("Korean", "안녕");
            translations.put("Selamat tinggal", malayTranslations);

            // Retrieve the translation from the map
            Map<String, String> targetTranslations = translations.getOrDefault(text, new HashMap<>());
            String translatedText = targetTranslations.getOrDefault(targetLanguage, "Translation not found");

            return translatedText;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ServerSide serverInterface = new ServerSide();
                serverInterface.setVisible(true);
            }
        });
    }
}



