package Java.ru.geekbrains.NetworkChat.swing;

import Java.ru.geekbrains.NetworkChat.TextMessage;

import javax.swing.*;
import java.awt.*;

public class TextMessageCellRenderer extends JPanel implements ListCellRenderer<TextMessage> {
    private final JLabel userName;
    private final JTextArea messageText;
    private final JLabel created;
    private final JPanel panel;

    public TextMessageCellRenderer() {

        userName = new JLabel();
        messageText = new JTextArea();
        created = new JLabel();

        panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(created);
        panel.add(userName);

        Font f = userName.getFont();
        userName.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
        messageText.setLineWrap(true);
        messageText.setWrapStyleWord(true);
        messageText.setEditable(false);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.WEST);

        add(messageText, BorderLayout.SOUTH);

    }


    @Override
    public Component getListCellRendererComponent(JList<? extends TextMessage> list, TextMessage value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        setBackground(list.getBackground());
        created.setText(value.getCreated());
        userName.setOpaque(true);
        userName.setText(value.getUserFrom());
        messageText.setText(value.getText());
        return this;
    }
}
