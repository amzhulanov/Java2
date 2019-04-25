package Java.ru.geekbrains.NetworkChat;

import Java.ru.geekbrains.NetworkChat.Authorization.AuthService;
import Java.ru.geekbrains.NetworkChat.Authorization.AuthServiceImp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ChatServer {

    private AuthService authService = new AuthServiceImp();
    //синхронизированная Мап хранит логин и всего пользователя. Синхронизация необходима для доступа к МАПе из всех потоков
    private Map<String, ClientHandler> clientHandlerMap = Collections.synchronizedMap(new HashMap<>());

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.start(7777);

    }

    private void start(int port) {
        Socket socket;
        try (ServerSocket serverSocket = new ServerSocket(7777)) {
            System.out.println("Сервер ожидает подключения!");
            while (true) {
                socket = serverSocket.accept();
                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());


                User user = null;
                try {
                    /*if (clientHandlerMap.get(user) != null) {
                        System.out.printf("В чате есть участникс таким ником", user.getLogin());
                        throw new AuthException();
                    }*/
                    String authMessage = in.readUTF();
                    user = checkAuthentication(authMessage);

                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (AuthException ex) {//Если авторизация не прошла, сообщаем об этом и закрываем сокет
                    out.writeUTF("/auth fails");
                    out.flush();
                    socket.close();
                }

                if (user != null && authService.authUser(user) && clientHandlerMap.get(user) == null) {
                    //если авторизация прошла, то записываем данные пользователя в МАПу
                    clientHandlerMap.put(user.getLogin(), new ClientHandler(user.getLogin(), socket, this));
                    out.writeUTF("/auth successful");
                    out.flush();
                    System.out.println("Подключился " + user.getLogin());
                } else {

                    if (user != null) {
                        System.out.printf("Wrong authorization for user %s%n", user.getLogin());
                    }
                    out.writeUTF("/auth fails");
                    out.flush();
                    socket.close();
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка при открытии ServerSocket. " + e);
        }
    }

    private User checkAuthentication(String authMessage) throws AuthException {
        String[] authParts = authMessage.split(" ");
        if (authParts.length != 3 || !authParts[0].equals("/auth")) {
            System.out.printf("Incorrect authorization message %s%n", authMessage);
            throw new AuthException();

        }
        return new User(authParts[1], authParts[2]);//передаём данные введённого пользователя
    }

    public void sendMessage(String userTo, String userFrom, String msg) throws IOException {
        ClientHandler userToClientHandler = clientHandlerMap.get(userTo);
        if (userToClientHandler != null) {
            userToClientHandler.sendMessage(userTo, userFrom, msg);//сервер отправляет уже разбитое сообщение
        }

    }


}
