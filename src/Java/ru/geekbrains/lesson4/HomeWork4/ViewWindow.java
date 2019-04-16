package Java.ru.geekbrains.lesson4.HomeWork4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewWindow extends JFrame {
    private final JTextField message;
    private final TextMessageCellRenderer messageCellRenderer;
    private final JList<TextMessage> jList;
    private  final DefaultListModel<TextMessage> jListModel;
    private final JPanel mainPanel;
    private final JScrollPane scroll;
    private final JButton buttonEnter;

    public ViewWindow() {
        setTitle("Chat");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(200, 200, 500, 500);
        mainPanel = new JPanel();
         buttonEnter = new JButton("Отправить");
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        jList = new JList<>();
        jListModel = new DefaultListModel<>();
        messageCellRenderer=new TextMessageCellRenderer();
        jList.setModel(jListModel);
        jList.setCellRenderer(messageCellRenderer);
        message = new JTextField();


        scroll=new JScrollPane(jList,
                        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll,BorderLayout.CENTER);

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                String text=message.getText().trim();
                if (message.getText().trim().length() > 0) {
                    TextMessage msg=new TextMessage("Вы",text);
                    jListModel.add(jListModel.getSize(), msg);
                    message.setText(null);
                }


            }
        };
        message.addActionListener(actionListener);
        mainPanel.add(message);
        mainPanel.add(buttonEnter);
        buttonEnter.addActionListener(actionListener);
        add(mainPanel, BorderLayout.SOUTH);
        setVisible(true);
    }
}
