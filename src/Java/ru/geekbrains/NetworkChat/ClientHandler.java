package Java.ru.geekbrains.NetworkChat;

import Java.ru.geekbrains.NetworkChat.swing.ViewWindow;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import static Java.ru.geekbrains.NetworkChat.MessagePatterns.parseTextMessageRegx;
import static Java.ru.geekbrains.NetworkChat.MessagePatterns.*;

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
                        System.out.printf("Message from user %s: %s%n", login, msg);
                        System.out.println("New message " + msg);
                        TextMessage message = parseTextMessageRegx(msg);//разбиваю поступившее на сервер сообщение на части

                        if (msg.equals(DISCONNECTED)) {
                            System.out.printf("User %s is disconnected%n", login);
                            chatServer.unsubscribe(login);
                            return;
                        } else if (msg != null) {
                            chatServer.sendMessage(message);//userTo,userFrom,text

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
}
