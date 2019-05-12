package Java.ru.geekbrains.NetworkChat;

import Java.ru.geekbrains.NetworkChat.Authorization.AuthService;
import Java.ru.geekbrains.NetworkChat.Authorization.AuthServiceJdbcImpl;
import Java.ru.geekbrains.NetworkChat.Exception.AuthException;
import Java.ru.geekbrains.NetworkChat.Persistance.UserRepository;

import javax.security.auth.login.LoginException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static Java.ru.geekbrains.NetworkChat.MessagePatterns.AUTH_SUCCESS_RESPONSE;
import static Java.ru.geekbrains.NetworkChat.MessagePatterns.AUTH_FAIL_RESPONSE;
import static Java.ru.geekbrains.NetworkChat.MessagePatterns.AUTH_LOGIN_FAIL_RESPONSE;

public class ChatServer {

    public static AuthService authService;// = new AuthServiceJdbcImpl();
    //синхронизированная Мап хранит логин и всего пользователя. Синхронизация необходима для доступа к МАПе из всех потоков
    public static Map<String, ClientHandler> clientHandlerMap = Collections.synchronizedMap(new HashMap<>());
    private MessageReciever userReciever;
    public static UserRepository userRepository;
    public static void main(String[] args) throws InterruptedException, SQLException {

        try {
            Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/network_chat?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Novosibirsk","root","Fqtlfqk");

            userRepository = new UserRepository(con);
            authService = new AuthServiceJdbcImpl(userRepository);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        ChatServer chatServer = new ChatServer();
        chatServer.start(7777);

    }

    private void start(int port) throws InterruptedException {
        Socket socket;
        this.userReciever=userReciever;
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
                }catch (SQLException ex){
                    System.out.println("В сети уже есть пользователь с таким именем");
                    out.writeUTF(AUTH_LOGIN_FAIL_RESPONSE);
                    out.flush();
                    break;
                }catch (AuthException ex) {//Если авторизация не прошла, сообщаем об этом и закрываем сокет
                    out.writeUTF(AUTH_FAIL_RESPONSE);
                    out.flush();
                    break;
                } catch (LoginException ex) {

                    System.out.println("В сети уже есть пользователь с таким именем");
                    out.writeUTF(AUTH_LOGIN_FAIL_RESPONSE);
                    out.flush();
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
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка при открытии ServerSocket. " + e);
        }
    }

    private User checkAuthentication(String authMessage) throws AuthException, LoginException, SQLException {
        String[] authParts = authMessage.split(" ");

        if (authParts.length != 3 || !authParts[0].equals("/auth")) {
            throw new AuthException();
        }
        if (ChatServer.loginIsBusy(authParts[1])) {
            throw new LoginException("Указанное имя уже занято");
        }
        return new User(-1,authParts[1], authParts[2]);//передаём данные введённого пользователя
    }

    private void sendUserConnectedMessage(String login) throws IOException {
        for (ClientHandler clientHandler : clientHandlerMap.values()) {
            clientHandler.sendConnectedMessage(login);
            //логин не может совпадать, т.к. новый пользователь ещё не добавлен в МАПу
            //отправляю сообщение о вновь подключившемся
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

    public void subscribe(String login, Socket socket) throws IOException, InterruptedException {
        ClientHandler clientHandler=new ClientHandler(login, socket, this);
        clientHandlerMap.put(login, clientHandler);
        sendUserConnectedMessage(login);//метод отправки пользователям сообщение, с логином нового пользователя

    }

    public static void unsubscribe(String login) throws IOException {
        clientHandlerMap.remove(login);
        for (ClientHandler clientHandler : clientHandlerMap.values()) {
           // System.out.printf("Sending disconnect notification to %s about %s%n", clientHandler.getLogin(), login);
            clientHandler.sendDisconnectedMessage(login);//отправляю каждому клиенту сообщение что пользователь отключился
        }
    }

    public static boolean loginIsBusy(String login) throws SQLException {
        //метод проверяет есть ли такой пользователь уже в сети
        if (clientHandlerMap.get(login) != null) {
            return true;//возвращает true, если пользователь в сети
        } else {
            return false;//возвращает false, если пользователя нет в сети
        }
    }

    public Set<String> getUserList() {
        return Collections.unmodifiableSet(clientHandlerMap.keySet());
    }

}
