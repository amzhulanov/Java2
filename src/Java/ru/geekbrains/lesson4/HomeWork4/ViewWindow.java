package Java.ru.geekbrains.lesson4.HomeWork4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static Java.ru.geekbrains.lesson4.HomeWork4.ChatController.sendText;

public class ViewWindow extends JFrame {
    private static JTextField message = new JTextField();
    public static DefaultListModel<String> textArea = new DefaultListModel<String>();

    public ViewWindow() {
        setTitle("Chat");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(200, 200, 500, 500);
        JPanel mainPanel = new JPanel();
        JButton buttonEnter = new JButton("Отправить");
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        JList<String> jList = new JList<String>(textArea);
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                sendText(message.getText().trim());
                validate();
                message.setText("");
            }
        };
        message.addActionListener(actionListener);
        mainPanel.add(message);
        mainPanel.add(buttonEnter);
        buttonEnter.addActionListener(actionListener);
        add(new JScrollPane(jList), BorderLayout.CENTER);
        add(mainPanel, BorderLayout.SOUTH);
        setVisible(true);
    }
}
