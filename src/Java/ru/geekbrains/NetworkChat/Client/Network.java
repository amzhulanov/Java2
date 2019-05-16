package Java.ru.geekbrains.NetworkChat.Client;

import Java.ru.geekbrains.NetworkChat.Server.ChatServer;
import Java.ru.geekbrains.NetworkChat.Server.Exception.AuthException;
import Java.ru.geekbrains.NetworkChat.Server.Exception.LoginNonExistent;
import Java.ru.geekbrains.NetworkChat.Server.Exception.RegPasswordException;

import javax.security.auth.login.LoginException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Set;

import static Java.ru.geekbrains.NetworkChat.Client.MessagePatterns.*;

public class Network {

    public Socket socket;
    public DataInputStream in;
    public DataOutputStream out;

    private String login;
    private String hostname;
    private int port;
    private MessageReciever messageReciever;
    private HistoryMessage historyMessage=new HistoryMessage();

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
                            //сохраняю текст сообщения в файл получателя
                            historyMessage.writeMessage(textMessage.getCreated(),textMessage.getUserTo(),textMessage.getUserFrom(),textMessage.getText());//время, имя файла, от кого, текст
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

            historyMessage.createFile(historyMessage.createFolder(),login);
            historyMessage.readMessage(login,messageReciever);
            receiverThread.start();

        } else if (response.equals(AUTH_LOGIN_FAIL_RESPONSE)) {//если сработало исключение по занятости имени
            throw new LoginException();
        } else if (response.equals(AUTH_LOGIN_NON_EXISTENT)) {//если сработало исключение по отсутствию имени
            throw new LoginNonExistent();
        } else {
            throw new AuthException();

        }
    }

    public void registration(String login, String password, String passwordRepeat) throws IOException, LoginException, RegPasswordException {
        socket = new Socket(hostname, port);
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());

        if (!password.equals((passwordRepeat)) || password.isEmpty()) {//введённые пароли должны совпадать
            throw new RegPasswordException();
        } else if (login.isEmpty()) {//имя не должно быть пустым
            throw new LoginException();
        }
        sendMessage(String.format(REG_PATTERN, login, password));
        String response = in.readUTF();
        if (response.equals(AUTH_SUCCESS_RESPONSE) || response.equals(String.format(CONNECTED_SEND, login))) {
            this.login = login;

            receiverThread.start();

        }
    }

    public void sendTextMessage(TextMessage message) {//метод используется при отправке сообщения из чата-клиента серверу
        try {//сохраняю сообщение в файл отправителя, непосредственно перед отправкой
            historyMessage.writeMessage(message.getCreated(),message.getUserFrom(),message.getUserFrom(),message.getText());//время, имя файла, от кого, текст
        } catch (IOException e) {
            e.printStackTrace();
        }
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

