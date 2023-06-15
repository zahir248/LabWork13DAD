// Exercise 7

package demotcpclientdate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ClientSide2 extends JFrame {
    private static final int SERVER_PORT = 8085;

    private JTextField textInput;
    private JComboBox<String> languageComboBox;
    private JTextArea translationArea;

    public ClientSide2() {
        setTitle("Text Translation Client");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create components
        JLabel textLabel = new JLabel("English Text:");
        textInput = new JTextField();
        JLabel languageLabel = new JLabel("Target Language:");
        languageComboBox = new JComboBox<>(new String[]{"Bahasa Malaysia", "Arabic", "Korean"});
        JButton translateButton = new JButton("Translate");
        translationArea = new JTextArea();
        translationArea.setEditable(false);

        // Create layout
        setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(textLabel);
        inputPanel.add(textInput);
        inputPanel.add(languageLabel);
        inputPanel.add(languageComboBox);
        inputPanel.add(new JLabel());
        inputPanel.add(translateButton);
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(translationArea), BorderLayout.CENTER);

        // Add translate button action listener
        translateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textInput.getText();
                String targetLanguage = (String) languageComboBox.getSelectedItem();

                // Perform translation
                String translatedText = translateText(text, targetLanguage);

                // Display translated text
                translationArea.setText(translatedText);
            }
        });
    }

    private String translateText(String text, String targetLanguage) {
        try {
            // Connect to the server
            Socket socket = new Socket(InetAddress.getLocalHost(), SERVER_PORT);

            BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // Send the text and target language to the server
            writer.println(text);
            writer.println(targetLanguage);

            // Receive the translated text from the server
            String translatedText = serverReader.readLine();

            // Close the connection
            socket.close();

            return translatedText;
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: Unable to connect to the server";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClientSide clientInterface = new ClientSide();
            clientInterface.setVisible(true);
        });
    }
}

