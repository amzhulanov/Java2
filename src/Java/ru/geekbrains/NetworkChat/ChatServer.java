package Java.ru.geekbrains.NetworkChat;

import Java.ru.geekbrains.NetworkChat.Authorization.AuthService;
import Java.ru.geekbrains.NetworkChat.Authorization.AuthServiceImp;
import Java.ru.geekbrains.NetworkChat.swing.LoginDialog;

import javax.security.auth.login.LoginException;
import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static Java.ru.geekbrains.NetworkChat.MessagePatterns.AUTH_SUCCESS_RESPONSE;
import static Java.ru.geekbrains.NetworkChat.MessagePatterns.AUTH_FAIL_RESPONSE;
import static Java.ru.geekbrains.NetworkChat.MessagePatterns.AUTH_LOGIN_FAIL_RESPONSE;

public class ChatServer {

    private AuthService authService = new AuthServiceImp();
    //синхронизированная Мап хранит логин и всего пользователя. Синхронизация необходима для доступа к МАПе из всех потоков
    private static Map<String, ClientHandler> clientHandlerMap = Collections.synchronizedMap(new HashMap<>());

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
                    String authMessage = in.readUTF();
                    user = checkAuthentication(authMessage);

                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (AuthException ex) {//Если авторизация не прошла, сообщаем об этом и закрываем сокет
                    out.writeUTF(AUTH_FAIL_RESPONSE);
                    out.flush();
                    break;
                    //  socket.close();
                } catch (LoginException ex) {

                    System.out.println("В сети уже есть пользователь с таким именем");
                    out.writeUTF(AUTH_LOGIN_FAIL_RESPONSE);
                    out.flush();
                    //socket.close();
                    break;
                }


                if (user != null && authService.authUser(user)) {
                    //если авторизация прошла, то записываем данные пользователя в МАПу
                    System.out.println("Подключился пользователь " + user.getLogin());
                    subscribe(user.getLogin(), socket);
                    out.writeUTF(AUTH_SUCCESS_RESPONSE);
                    out.flush();

                } else {
                    if (user != null) {
                        System.out.printf("Wrong authorization for user %s%n", user.getLogin());
                    }
                    out.writeUTF(AUTH_FAIL_RESPONSE);
                    out.flush();
                    // socket.close();
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка при открытии ServerSocket. " + e);
        }
    }

    private User checkAuthentication(String authMessage) throws AuthException, LoginException {
        String[] authParts = authMessage.split(" ");

        if (authParts.length != 3 || !authParts[0].equals("/auth")) {
            System.out.printf("Incorrect authorization message %s%n", authMessage);
            throw new AuthException();
        }
        if (ChatServer.loginIsBusy(authParts[1])) {
            throw new LoginException("Указанное имя уже занято");
        }
        return new User(authParts[1], authParts[2]);//передаём данные введённого пользователя

    }

    private void sendUserConnectedMessage(String login) throws IOException {
        for (ClientHandler clientHandler : clientHandlerMap.values()) {
            System.out.printf("Sending connect notification to %s about %s%n", clientHandler.getLogin(), login);
            clientHandler.sendConnectedMessage(login);//отправляю каждому пользователю сообщение о присоединившемся
            /*if (!clientHandler.getLogin().equals(login)) {
                System.out.printf("Sending connect notification to %s about %s%n", clientHandler.getLogin(), login);
                clientHandler.sendConnectedMessage(login);
            }*/
        }
    }

    public void sendMessage(TextMessage msg) throws IOException {
        ClientHandler userToClientHandler = clientHandlerMap.get(msg.getUserTo());
        if (userToClientHandler != null) {
            userToClientHandler.sendMessage(msg.getUserTo(), msg.getUserFrom(), msg.getText());//сервер отправляет уже разбитое сообщение
        } else {
            System.out.printf("User %s not connected%n", msg.getUserTo());
        }


    }

    public void subscribe(String login, Socket socket) throws IOException {
        clientHandlerMap.put(login, new ClientHandler(login, socket, this));
        sendUserConnectedMessage(login);//метод отправки пользователям сообщение, с логином нового пользователя
    }

    public static void unsubscribe(String login) {
        clientHandlerMap.remove(login);

        // TODO Отправить всем подключенным пользователям сообщение, что данный пользователь отключился
    }

    public static boolean loginIsBusy(String login) {//если логин занят, возвращаю True
        //ClientHandler clientHandler=clientHandlerMap.get(login);
        if (clientHandlerMap.get(login) != null) {
            return true;
        } else {
            return false;
        }

    }
}
