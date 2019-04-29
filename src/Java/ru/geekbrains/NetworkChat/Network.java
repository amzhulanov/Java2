package Java.ru.geekbrains.NetworkChat;

import javax.security.auth.login.LoginException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Collections;
import java.util.List;


import static Java.ru.geekbrains.NetworkChat.MessagePatterns.*;


public class Network {

    public Socket socket;
    public DataInputStream in;
    public DataOutputStream out;

    private String login;
    private String hostname;
    private int port;
    private MessageReciever messageReciever;

    private Thread receiverThread; //поток для считывания сообщения и отображения на форме

    //метод обрабатывает входящее на сокет сообщение
    public Network(String hostname, int port, MessageReciever messageReciever) {
        this.hostname = hostname;
        this.port = port;
        this.messageReciever = messageReciever;

        this.receiverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        String text = in.readUTF();

                        System.out.println("New message " + text);
                        TextMessage textMessage = parseTextMessageRegx(text);//Разбиваю полученный текст. Определяю отправителя, получателя и текст
                        if (textMessage != null) {
                            messageReciever.submitMessage(textMessage);
                            continue;
                        }
                        System.out.println("Connection message " + text);
                        String login = parseConnectedMessage(text);
                        if (login != null) {
                            messageReciever.userConnected(login);
                            continue;
                        }
                        // TODO добавить обработку отключения пользователя
                        System.out.println("Disconnection message " + text);
                        login = parseDisconnectedMessage(text);
                        if (login != null) {
                            messageReciever.userDisconnected(login);
                            continue;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (socket.isClosed()) {
                            break;
                        }
                    }

                }
            }
        });
    }

    public void authorize(String login, String password) throws IOException, AuthException, LoginException {
        socket = new Socket(hostname, port);
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());

        sendMessage(String.format(AUTH_PATTERN, login, password));
        String response = in.readUTF();
        if (response.equals(AUTH_SUCCESS_RESPONSE)||response.equals(String.format(CONNECTED_SEND,login))) {
            this.login = login;

            receiverThread.start();
        } else if (response.equals(AUTH_LOGIN_FAIL_RESPONSE)) {//если сработало исключение по занятости имени
            throw new LoginException();
        } else {
            throw new AuthException();

        }
    }

    public void sendTextMessage(TextMessage message) {//метод используется при отправке сообщения из чата-клиента серверу
        sendMessage(String.format(MESSAGE_SEND_PATTERN, message.getUserTo(), message.getUserFrom(), message.getText()));
    }
    private void sendMessage(String msg) { //отправляет сформированную строку строчку через сеть
        try {
            out.writeUTF(msg);//msg содержит Логин, UserTo и текст сообщения
            out.flush();//flush для надёжного "проталкивания" сообщения в writeUTF"
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public List<String> requestConnectedUserList() {

        // TODO реализовать запрос с сервера списка всех подключенных пользователей
        return Collections.emptyList();
    }

    public String getLogin() {
        return login;
    }

    //@Override
    public void close() throws IOException {
        ChatServer.unsubscribe(login);
        this.receiverThread.interrupt();
        sendMessage(DISCONNECTED);
    }
}

