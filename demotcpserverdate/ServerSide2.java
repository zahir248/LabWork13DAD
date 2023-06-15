// Exercise 7

package demotcpserverdate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerSide2 extends JFrame {
    private static final int SERVER_PORT = 8085;

    private JLabel requestCounterLabel;
    private JTable requestLogTable;
    private DefaultTableModel tableModel;

    private int requestCounter = 0;
    private List<String[]> requestLog = new ArrayList<>();

    public ServerSide2() {
        setTitle("Text Translation Server");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create components
        requestCounterLabel = new JLabel("Request Counter: " + requestCounter);
        tableModel = new DefaultTableModel(new Object[]{"Request", "Response"}, 0);
        requestLogTable = new JTable(tableModel);

        // Create layout
        setLayout(new BorderLayout());
        add(requestCounterLabel, BorderLayout.NORTH);
        add(new JScrollPane(requestLogTable), BorderLayout.CENTER);

        // Start the server
        startServer();
    }

    private void startServer() {
        try {
            // Create a server socket
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Server started. Listening on port " + SERVER_PORT);

            // Start accepting client connections
            while (true) {
                // Accept client connection
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Handle client request in a separate thread
                new Thread(() -> handleClientRequest(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClientRequest(Socket clientSocket) {
        try {
            // Create input and output streams
            BufferedReader clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

            // Read the text and target language from the client
            String text = clientReader.readLine();
            String targetLanguage = clientReader.readLine();

            // Translate the text based on the target language
            String translatedText = translateText(text, targetLanguage);

            // Update the request counter and request log
            requestCounter++;
            String[] requestDetails = {text, translatedText};
            requestLog.add(requestDetails);

            // Update the GUI interface
            SwingUtilities.invokeLater(() -> {
                requestCounterLabel.setText("Request Counter: " + requestCounter);
                tableModel.addRow(requestDetails);
            });

            // Send the translated text to the client
            writer.println(translatedText);

            // Close the client socket
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String translateText(String text, String targetLanguage) {
        // Perform the translation based on the provided text and target language
        // You can implement your translation logic here
        // For simplicity, let's assume we have a map of translations

        Map<String, Map<String, String>> translations = new HashMap<>();

        // English translations
        Map<String, String> englishTranslations = new HashMap<>();
        englishTranslations.put("Bahasa Malaysia", "Selamat pagi");
        englishTranslations.put("Arabic", "الخير صباح");
        englishTranslations.put("Korean", "좋은 아침");
        translations.put("Good morning", englishTranslations);

        // Add translations for other phrases
        Map<String, String> malayTranslations = new HashMap<>();
        malayTranslations.put("English", "Goodbye");
        malayTranslations.put("Arabic", "مع السلامة");
        malayTranslations.put("Korean", "안녕");
        translations.put("Selamat tinggal", malayTranslations);

        Map<String, String> arabicTranslations = new HashMap<>();
        arabicTranslations.put("English", "Good night");
        arabicTranslations.put("Bahasa Malaysia", "Selamat malam");
        arabicTranslations.put("Korean", "안녕히 주무세요");
        translations.put("مساء الخير", arabicTranslations);

        Map<String, String> koreanTranslations = new HashMap<>();
        koreanTranslations.put("English", "What's up?");
        koreanTranslations.put("Bahasa Malaysia", "Ada apa?");
        koreanTranslations.put("Arabic", "أخبارك؟");
        translations.put("잘 지내세요?", koreanTranslations);

        // Retrieve the translation from the map
        Map<String, String> targetTranslations = translations.getOrDefault(text, new HashMap<>());
        String translatedText = targetTranslations.getOrDefault(targetLanguage, "Translation not found");

        return translatedText;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ServerSide2 serverInterface = new ServerSide2();
            serverInterface.setVisible(true);
        });
    }
}
