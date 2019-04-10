package Java.ru.geekbrains.lesson4.HomeWork4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame {
    private static JTextField message = new JTextField();
    private static JTextArea textArea = new JTextArea();

    public MainWindow() {
        setTitle("Chat");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(200, 200, 500, 500);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        JScrollPane scrollBar = new JScrollPane(textArea);
        JPanel mainPanel = new JPanel();
        JButton buttonEnter = new JButton("Отправить");
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                sendText();

            }
        };
        message.addActionListener(actionListener);
        mainPanel.add(message);
        mainPanel.add(buttonEnter);
        buttonEnter.addActionListener(actionListener);
        add(scrollBar, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private static void sendText() {
        if (message.getText().trim().length() > 0) {
            textArea.append(message.getText().trim() + "\n");
            message.setText("");
        }
    }
}
