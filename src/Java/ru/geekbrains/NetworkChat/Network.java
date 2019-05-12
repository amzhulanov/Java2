package Java.ru.geekbrains.NetworkChat;

import Java.ru.geekbrains.NetworkChat.Exception.AuthException;

import javax.security.auth.login.LoginException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Set;

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
                        TextMessage textMessage = parseTextMessageRegx(text);//Разбиваю полученный текст. Определяю отправителя, получателя и текст
                        if (textMessage != null) {
                            messageReciever.submitMessage(textMessage);
                            continue;
                        }
                        String login = parseConnectedMessage(text);
                        if (login != null) {
                            messageReciever.userConnected(login);
                            continue;
                        }
                        login = parseDisconnectedMessage(text);
                        if (login != null) {
                            messageReciever.userDisconnected(login);
                            continue;
                        }
                        Set<String> users = parseUserList(text);
                        if (users != null) {
                            messageReciever.updateUserList(users);
                        }
                    } catch (SocketException e) {
                        try {
                            ChatServer.unsubscribe(login);
                        } catch (IOException ex) {
                            ex.printStackTrace();
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
        if (response.equals(AUTH_SUCCESS_RESPONSE) || response.equals(String.format(CONNECTED_SEND, login))) {
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

    public void requestConnectedUserList() {
        sendMessage(USER_LIST_TAG);
    }

    public String getLogin() {
        return login;
    }

    public void close() {
        this.receiverThread.interrupt();
        sendMessage(DISCONNECTED);
    }
}

