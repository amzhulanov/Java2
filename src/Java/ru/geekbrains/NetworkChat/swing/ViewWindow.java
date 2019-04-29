package Java.ru.geekbrains.NetworkChat.swing;

import Java.ru.geekbrains.NetworkChat.ChatServer;
import Java.ru.geekbrains.NetworkChat.MessageReciever;
import Java.ru.geekbrains.NetworkChat.Network;
import Java.ru.geekbrains.NetworkChat.TextMessage;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class ViewWindow extends JFrame implements MessageReciever {
    private final JTextField message, nick;
    private final TextMessageCellRenderer messageCellRenderer;
    private final JList<TextMessage> jList;
    private final DefaultListModel<TextMessage> jListModel;
    private final JPanel mainPanel;
    private final JScrollPane scroll;
    private final JButton buttonEnter;
    private final Network network;
    private final DefaultListModel<String> listUserModel;
    private final JList<String> listUser;

    public  ViewWindow() {
        setTitle("ChatClient");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(200, 200, 500, 500);
         mainPanel = new JPanel();

        buttonEnter = new JButton("Отправить");
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        jList = new JList<>();
        jListModel = new DefaultListModel<>();//список, содержащий всех подключенных клиентов
        messageCellRenderer = new TextMessageCellRenderer();
        jList.setModel(jListModel);
        jList.setCellRenderer(messageCellRenderer);

        message = new JTextField("Введите сообщение");//текстовое поле для ввода сообщения
        nick = new JTextField("Имя пользователя",5);//поле для ввода имени пользователя получателя сообщения
        Font f = nick.getFont();
        nick.setFont(f.deriveFont(f.getStyle() | Font.BOLD));

        scroll = new JScrollPane(jList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll, BorderLayout.CENTER);

        ActionListener actionListener = new ActionListener() {//обработка нажатия кнопки Отправить
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
                    System.out.println("Необходимо указать Имя пользователя и написать текст сообщения");
                }
            }
        };
        message.addActionListener(actionListener);
        buttonEnter.addActionListener(actionListener);

        mainPanel.add(nick,BorderLayout.WEST);
        mainPanel.add(message,BorderLayout.CENTER);
        mainPanel.add(buttonEnter, BorderLayout.EAST);

        add(mainPanel, BorderLayout.SOUTH);

        listUser = new JList<>();
        listUserModel = new DefaultListModel<>();
        listUser.setModel(listUserModel);
        listUser.setPreferredSize(new Dimension(100, 0));
        add(listUser, BorderLayout.WEST);

        listUser.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionListener actionListenerJList=new ListSelectionListener() {//проверяю выбор пользователя в списке
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!listUser.getValueIsAdjusting()) { // Игнорируем событие mouseDown
                    nick.setText(listUser.getSelectedValue().toString());//устанавливаю выбранное значение
                }
            }
        };
        listUser.addListSelectionListener(actionListenerJList);

        setVisible(true);

        this.network = new Network("localhost", 7777, this);


        LoginDialog loginDialog = new LoginDialog(this, network);
        loginDialog.setVisible(true);//вывожу окно ввода логин/пароль

        if (!loginDialog.isConnected()) {//если не подключился, то после закрытия окна логина приложение закрывается
            System.exit(0);
        }
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                    if (network != null) {
                        userDisconnected(network.getLogin());
                        try {
                            network.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    super.windowClosing(event);
                }
        });
        setTitle("ChatClient: " + network.getLogin());
    }

    @Override
    public void submitMessage(TextMessage message) {
        SwingUtilities.invokeLater(new Runnable() { //invokeLater используется, т.к. метод может
            // быть вызван из другого класса
            @Override
            public void run() {
                jListModel.add(jListModel.size(), message);//Выводим сообщение на экран клиента
                jList.ensureIndexIsVisible(jListModel.size() - 1);
            }
        });
    }
    @Override
    public void userConnected(String login) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int ix = listUserModel.indexOf(login); //если подключился пользователь, проверяе есть ли он в списке
                if (ix == -1) {//если пользователя нет, то мы добавляем в конец списка нового пользователя
                    listUserModel.add(listUserModel.size(), login);
                }
            }
        });
    }
    @Override
    public void userDisconnected(String login) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int ix = listUserModel.indexOf(login);//убираем пользователя из списка
                if (ix >= 0) {
                    listUserModel.remove(ix);
                }
            }
        });
    }

}
