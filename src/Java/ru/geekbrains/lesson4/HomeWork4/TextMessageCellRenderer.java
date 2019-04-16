package Java.ru.geekbrains.lesson4.HomeWork4;

import javax.swing.*;
import java.awt.*;

public class TextMessageCellRenderer extends JPanel implements ListCellRenderer<TextMessage> {
    private final JLabel userName;
    private final JTextArea messageText;
    private final JLabel created;

    public TextMessageCellRenderer() {

        userName = new JLabel();
        messageText = new JTextArea();
        created = new JLabel();

        Font f=userName.getFont();
        userName.setFont(f.deriveFont(f.getStyle()|Font.BOLD));
        messageText.setLineWrap(true);
        messageText.setWrapStyleWord(true);
        messageText.setEditable(false);

        setLayout(new BorderLayout());
        add(created, BorderLayout.EAST);
        add(userName, BorderLayout.WEST);
        add(messageText, BorderLayout.SOUTH);

    }


    @Override
    public Component getListCellRendererComponent(JList<? extends TextMessage> list, TextMessage value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        setBackground(list.getBackground());
        created.setText(value.getCreated());
        userName.setOpaque(true);
        userName.setText(value.getUserName());
        messageText.setText(value.getText());
        return this;
    }
}
