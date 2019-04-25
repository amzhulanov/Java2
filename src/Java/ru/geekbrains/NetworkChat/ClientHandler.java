package Java.ru.geekbrains.NetworkChat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static Java.ru.geekbrains.NetworkChat.Network.MESSAGE_SEND_PATTERN;

public class ClientHandler {
    private final String login;
    private final Socket socket;

    private final DataOutputStream out;
    private final DataInputStream in;

    private final Thread handleThread;
    private ChatServer chatServer;

    public ClientHandler(String login, Socket socket, ChatServer chatServer) throws IOException {
        this.login = login;
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
        this.chatServer=chatServer;

        this.handleThread=new Thread(new Runnable() {//поток для обработки входящих на сервер сообщений
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()){

                    try{
                        String msg = in.readUTF();//сервер слушает поступающие сообщения
                        System.out.printf("Message from user %s: %s%n",login,msg);
                        String[] message=msg.split(" "); //разбиваю поступившее на сервер сообщение на части
                        if (message[1]!=null){//сли не пустое имя клиента, то отправляю в SendMessage сервера
                            chatServer.sendMessage(message[1],message[2],msg.substring(message[0].length()+message[1].length()+message[2].length()+3));//userTo,userFrom,text
                        }

                    }catch(IOException ex){
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
        out.writeUTF(String.format(MESSAGE_SEND_PATTERN, userTo,userFrom, msg));//сообщение группируется по паттерну
    }

}
