package Java.ru.geekbrains.NetworkChat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Network {

    public static final String AUTH_PATTERN = "/auth %s %s";
    public static final String MESSAGE_SEND_PATTERN = "/w %s %s %s";

    public Socket socket;
    public DataInputStream in;
    public DataOutputStream out;

    private String login;
    private String hostname;
    private int port;

    private Thread receiverThread; //поток для считывания сообщения и отображения на форме

    //метод обрабатывает входящее на сокет сообщение
    public Network(String hostname, int port, MessageReciever messageReciever) {
        this.hostname = hostname;
        this.port = port;
        this.receiverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String text = in.readUTF();
                        String[] user = text.split(" ");//Разбиваю полученный текст. Определяю отправителя, получателя и текст
                        TextMessage textMessage = new TextMessage(user[1], user[2], text.substring(user[0].length() + user[1].length() + user[2].length() + 3)); //UserTo,UserFrom,Text
                        messageReciever.submitMessage(textMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }

    public void authorize(String login, String password) throws IOException, AuthException {
        socket = new Socket(hostname, port);
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());

        sendMessage(String.format(AUTH_PATTERN, login, password));
        String response = in.readUTF();
        if (response.equals("/auth successful")) {
            this.login = login;
            receiverThread.start();
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

    public String getLogin() {
        return login;
    }
}
