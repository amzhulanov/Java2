package Java.ru.geekbrains.NetworkChat.Server;


import Java.ru.geekbrains.NetworkChat.Client.HistoryMessage;
import Java.ru.geekbrains.NetworkChat.Client.TextMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Set;

import static Java.ru.geekbrains.NetworkChat.Client.MessagePatterns.parseTextMessageRegx;
import static Java.ru.geekbrains.NetworkChat.Client.MessagePatterns.*;

public class ClientHandler {
    private final String login;
    private final Socket socket;

    private final DataOutputStream out;
    private final DataInputStream in;

    private final Thread handleThread;
    private ChatServer chatServer;

    //СlientHandler создаётся для каждого клиента
    public ClientHandler(String login, Socket socket, ChatServer chatServer) throws IOException {
        this.login = login;
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
        this.chatServer = chatServer;


        this.handleThread = new Thread(new Runnable() {//поток для обработки входящих на сервер сообщений

            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {

                    try {
                        String msg = in.readUTF();//сервер слушает поступающие сообщения
                        TextMessage message = parseTextMessageRegx(msg);//разбиваю поступившее на сервер сообщение на части

                        if (msg.equals(DISCONNECTED)) {
                            chatServer.unsubscribe(login);
                            return;
                        } else if (msg.equals(USER_LIST_TAG)) {
                            sendUserList(chatServer.getUserList());
                        }/* else if (msg.equals(REG_LOGIN_BUSY)) {
                            //JOptionPane.showMessageDialog();
                            System.out.println("Показываю сообщение о занятости логина");
                        } */else if (msg != null) {
                            chatServer.sendMessage(message);//userTo,userFrom,text
                           // historyMessage.writeMessage(message.getCreated(),message.getUserFrom(),message.getUserTo(),message.getText());

                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        break;
                    }
                }
            }
        });
        this.chatServer = chatServer;
        this.handleThread.start();
    }

    public String getLogin() {
        return login;
    }

    public void sendMessage(String userTo, String userFrom, String msg) throws IOException {//метод отправки сообщения с сервера клиенту
        out.writeUTF(String.format(MESSAGE_SEND_PATTERN, userTo, userFrom, msg));
    }

    public void sendConnectedMessage(String login) throws IOException {

        if (socket.isConnected()) {
            out.writeUTF(String.format(CONNECTED_SEND, login));//если есть подключение, то отправляю строку для подключения
        }
    }

    public void sendDisconnectedMessage(String login) throws IOException {

        if (socket.isConnected()) {
            out.writeUTF(String.format(DISCONNECTED_SEND, login));//если есть подключение, то отправляю строку для отключения
        }
    }
    public void sendUserList(Set<String> users) throws IOException {
        if (socket.isConnected()) {
            out.writeUTF(String.format(USER_LIST_RESPONSE, String.join(" ", users)));
        }
    }
}
