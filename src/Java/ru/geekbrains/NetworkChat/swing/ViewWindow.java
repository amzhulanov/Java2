package Java.ru.geekbrains.NetworkChat.swing;

import Java.ru.geekbrains.NetworkChat.MessageReciever;
import Java.ru.geekbrains.NetworkChat.Network;
import Java.ru.geekbrains.NetworkChat.TextMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewWindow extends JFrame implements MessageReciever {
    private final JTextField message, nick;
    private final TextMessageCellRenderer messageCellRenderer;
    private final JList<TextMessage> jList;
    private final DefaultListModel<TextMessage> jListModel;
    private final JPanel mainPanel;
    private final JPanel panel;//панель для размещения списка выбора ника UserTo получателя сообщения
    private final JScrollPane scroll;
    private final JButton buttonEnter;
    private final Network network;
    private final String[] listUser = {};

    public ViewWindow() {
        setTitle("ChatClient");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(200, 200, 500, 500);
        mainPanel = new JPanel();

        buttonEnter = new JButton("Отправить");
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        jList = new JList<>();
        jListModel = new DefaultListModel<>();
        messageCellRenderer = new TextMessageCellRenderer();
        jList.setModel(jListModel);
        jList.setCellRenderer(messageCellRenderer);

        //создаю панель на которую помещаю поле для ввода Ника юзера и текст сообщения
        panel = new JPanel(new FlowLayout(FlowLayout.LEFT));//последовательное расположение слева
        message = new JTextField("Введите сообщение");//текстовое поле для ввода сообщения
        nick = new JTextField("Nick:");//поле для ввода имени пользователя получателя сообщения
        Font f = nick.getFont();
        nick.setFont(f.deriveFont(f.getStyle() | Font.BOLD));


        panel.add(nick);
        panel.add(message);

        scroll = new JScrollPane(jList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll, BorderLayout.CENTER);

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                String text = message.getText().trim();
                String userTo = nick.getText().trim();
                if (text.length() > 0 && userTo.length() > 0) {//проверяем, что сообщение не пустое и указан получатель
                    TextMessage msg = new TextMessage(userTo, network.getLogin(), text);//сообщение, которое отправляет клиент с указанием кому именно
                    jListModel.add(jListModel.getSize(), msg);
                    message.setText(null);

                    network.sendTextMessage(msg);//отправка сообщения
                } else {
                    System.out.println("Необходимо указать Nick и написать текст сообщения");
                }


            }
        };
        message.addActionListener(actionListener);
        mainPanel.add(panel);
        mainPanel.add(buttonEnter);
        buttonEnter.addActionListener(actionListener);
        add(mainPanel, BorderLayout.SOUTH);
        setVisible(true);

        this.network = new Network("localhost", 7777, this);
        ;

        LoginDialog loginDialog = new LoginDialog(this, network);
        loginDialog.setVisible(true);//вывожу окно ввода логин/пароль

        if (!loginDialog.isConnected()) {//если не подключился, то после закрытия окна логина приложение закрывается
            System.exit(0);
        }
        setTitle("ChatClient: " + network.getLogin());
    }

    @Override
    public void submitMessage(TextMessage message) {
        SwingUtilities.invokeLater(new Runnable() { //invokeLater используется, т.к. метод может
            // быть вызван из другого класса
            @Override
            public void run() {
                jListModel.add(jListModel.size(), message);//Добавляем сообщения в список вновь пришедших
                jList.ensureIndexIsVisible(jListModel.size() - 1);
            }
        });
    }

}
