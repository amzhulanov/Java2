package Java.ru.geekbrains.NetworkChat.Server;

import Java.ru.geekbrains.NetworkChat.Server.Authorization.AuthService;
import Java.ru.geekbrains.NetworkChat.Server.Authorization.AuthServiceJdbcImpl;
import Java.ru.geekbrains.NetworkChat.Client.TextMessage;
import Java.ru.geekbrains.NetworkChat.Server.EventLog.EventLogJdbcImpl;
import Java.ru.geekbrains.NetworkChat.Server.Exception.AuthException;
import Java.ru.geekbrains.NetworkChat.Server.Exception.LoginNonExistent;
import Java.ru.geekbrains.NetworkChat.Server.Exception.RegLoginException;
import Java.ru.geekbrains.NetworkChat.Server.Persistance.UserRepository;


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
import java.util.concurrent.*;
import java.util.logging.*;

import static Java.ru.geekbrains.NetworkChat.Client.MessagePatterns.*;

public class ChatServer {

    public static AuthService authService;
    public static EventLogJdbcImpl eventLogJdbc;
    //синхронизированная Мап хранит логин и всего пользователя. Синхронизация необходима для доступа к МАПе из всех потоков
    public static Map<String, ClientHandler> clientHandlerMap = Collections.synchronizedMap(new HashMap<>());
    public static final Logger logger = Logger.getLogger(ChatServer.class.getName());
    public static Connection con;
    public static volatile int countCurrentThread = 0;
    public final static int MAX_THREAD = 2;
    final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(100);
    private static ExecutorService executorService = new ThreadPoolExecutor(MAX_THREAD, MAX_THREAD, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(2), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thr = Executors.defaultThreadFactory().newThread(r);
            thr.setDaemon(true);
            return thr;
        }
    });


    public static void main(String[] args) throws SQLException {

        //LogManager.getLogManager().readConfiguration(ChatServer.class.getClassLoader()
        //        .getResourceAsStream("jul.properties"));
        connectionMySQL();
        eventLogJdbc=new EventLogJdbcImpl(con);
        logger.setLevel(Level.FINE);
        logger.getParent().setLevel(Level.FINE);
        logger.getParent().getHandlers()[0].setLevel(Level.FINE);

        logger.addHandler(new Handler() {
            @Override
            public void publish(LogRecord record) {
                eventLogJdbc.insertLog(record);
            }

            @Override
            public void flush() {
                flush();
            }

            @Override
            public void close() throws SecurityException {
            }
        });
        logger.info("Начало логирования");
        ChatServer chatServer = new ChatServer();
        chatServer.start(7777);

    }

    private static void connectionMySQL() {
        UserRepository userRepository;
        try {
             con = DriverManager.getConnection("jdbc:mysql://localhost:3306/network_chat?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Novosibirsk", "root", "localhost_1");

            userRepository = new UserRepository(con);
            authService = new AuthServiceJdbcImpl(userRepository);
            logger.fine("Server подключился к БД");
        } catch (SQLException e) {

            logger.fine(e.getMessage());
            return;
        }
    }

    private void start(int port) {
        Socket socket;
        try (ServerSocket serverSocket = new ServerSocket(7777)) {
            logger.info("Сервер ожидает подключения!");
            while (true) {
                socket = serverSocket.accept();
                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                User user = null;
                String authMessage = "";
                try {
                    authMessage = in.readUTF();
                    user = checkAuthentication(authMessage);
                } catch (IOException ex) {
                    logger.fine(ex.getMessage());
                } catch (AuthException ex) {//Если авторизация не прошла, сообщаем об этом и закрываем сокет
                    out.writeUTF(AUTH_FAIL_RESPONSE);
                    out.flush();
                    logger.info("Авторизация не прошла: текст полученного сообщения - " + authMessage);
                    continue;
                } catch (LoginException ex) {
                    // System.out.println("В сети уже есть пользователь с именем: " + user.getLogin());
                    logger.info("В сети уже есть пользователь с именем: " + user.getLogin());
                    out.writeUTF(AUTH_LOGIN_FAIL_RESPONSE);
                    out.flush();
                    continue;
                }
                boolean chResourse = checkResourceThread();
                if (authMessage.substring(0, 4).equals("/reg") && chResourse) {
                    try {
                        authService.registrationUser(user);
                    } catch (RegLoginException e) {
                        logger.fine(e.getMessage());
                        out.writeUTF(REG_FAIL_RESPONSE);
                        continue;
                    }
                    logger.info("Зарегистрирован пользователь: " + user.getLogin());
                    out.writeUTF(REG_SUCCESS_RESPONSE);
                    out.flush();
                    continue;
                } else {


                    try {
                        if (user != null && authService.authUser(user) && chResourse) {
                            //если авторизация прошла, то записываем данные пользователя в МАПу
                            //System.out.println("Подключился пользователь " + user.getLogin());
                            logger.info("Подключился пользователь " + user.getLogin());
                            subscribe(user.getLogin(), socket);
                            //mon.wait();
                            out.writeUTF(AUTH_SUCCESS_RESPONSE);
                            out.flush();
                            continue;
                        } else if (user != null && chResourse) {
                            //System.out.printf("Wrong authorization for user %s%n", user.getLogin());
                            logger.warning("Не прошла авторизация пользователя " + user.getLogin());
                            out.writeUTF(AUTH_FAIL_RESPONSE);
                            out.flush();
                        } else if (!chResourse) {
                            //System.out.println("Ресурсы сервера заняты");
                            logger.warning("Ресурсы сервера заняты");
                            out.writeUTF(NOT_THREAD);
                            out.flush();
                        }
                    } catch (LoginNonExistent loginNonExistent) {
                        //System.out.println("Пользователь не зарегистрирован");
                        logger.info("Пользователь не зарегистрирован");
                        out.writeUTF(AUTH_LOGIN_NON_EXISTENT);
                        out.flush();
                        continue;
                    }

                }


                    /*} catch (RegLoginException e) {
                        out.writeUTF(REG_LOGIN_BUSY);
                        out.flush();
                        break;
                    }*/
            }

        } catch (
                IOException e) {
            //System.out.println("Ошибка при открытии ServerSocket. " + e);
            logger.warning("Ошибка при открытии ServerSocket. " + e);
        }

    }

    private User checkAuthentication(String authMessage) throws AuthException, LoginException {
        String[] authParts = authMessage.split(" ");

        if (authParts.length != 3 || (!authParts[0].equals("/auth") && !authParts[0].equals("/reg"))) {
            throw new AuthException();
        }
        if (ChatServer.loginIsBusy(authParts[1])) {
            logger.warning("Указанное имя уже занято - " + authParts[1]);
            throw new LoginException("Указанное имя уже занято");
        }
        return new User(-1, authParts[1], authParts[2]);//передаём данные введённого пользователя
    }

    private void sendUserConnectedMessage(String login){//throws IOException {
        for (ClientHandler clientHandler : clientHandlerMap.values()) {
            try {
                clientHandler.sendConnectedMessage(login);
            } catch (IOException e) {
                logger.fine(e.getMessage());
            }
            //логин не может совпадать, т.к. новый пользователь ещё не добавлен в МАПу
            //отправляю сообщение о вновь подключившемся
        }
    }

    private boolean checkResourceThread() {
        /*if (countCurrentThread == MAX_THREAD) {
            return false;
        } else {
            return true;
        }*/
        return true;
    }

    public void sendMessage(TextMessage msg) throws IOException {
        ClientHandler userToClientHandler = clientHandlerMap.get(msg.getUserTo());
        if (userToClientHandler != null) {
            userToClientHandler.sendMessage(msg.getUserTo(), msg.getUserFrom(), msg.getText());//сервер отправляет уже разбитое сообщение
        } else {
            //System.out.printf("User %s not connected%n", msg.getUserTo());
            logger.warning(String.format("Пользователь %s не подключен",msg.getUserTo()));
        }
    }

    public void subscribe(String login, Socket socket) throws IOException {
        ClientHandler clientHandler = new ClientHandler(login, socket, this, executorService);
        countCurrentThread++;
        logger.fine("Текущее кол-во потоков = " + countCurrentThread);
        /*try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        clientHandlerMap.put(login, clientHandler);
        sendUserConnectedMessage(login);//метод отправки пользователям сообщение, с логином нового пользователя

    }

    public static void unsubscribe(String login) throws IOException {
        clientHandlerMap.remove(login);
        countCurrentThread--;
        logger.fine(String.format("Всего занято потоков %s из %s возможных", countCurrentThread, MAX_THREAD));
        for (ClientHandler clientHandler : clientHandlerMap.values()) {
            clientHandler.sendDisconnectedMessage(login);//отправляю каждому клиенту сообщение что пользователь отключился
        }
    }

    public static boolean loginIsBusy(String login) {
        //метод проверяет есть ли такой пользователь уже в сети
        logger.fine(String.format("Логин %s занят ", login));
        if (clientHandlerMap.get(login) != null) {
            return true;//возвращает true, если пользователь в сети
        } else {
            return false;//возвращает false, если пользователя нет в сети
        }
    }

    public Set<String> getUserList() {
        logger.fine(String.format("Возвращаю клиенту %s список пользователей", clientHandlerMap.keySet()));
        return Collections.unmodifiableSet(clientHandlerMap.keySet());
    }

}
