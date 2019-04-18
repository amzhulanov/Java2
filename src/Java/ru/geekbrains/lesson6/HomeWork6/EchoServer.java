package Java.ru.geekbrains.lesson6.HomeWork6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;


public class EchoServer {

    static class OutputMessage implements Runnable {//класс для отправки сообщения от сервера клиенту

        private Socket socket;
        private ArrayList<Socket> mySockets;//массив для хранения id клиентов и сокетов

        private OutputMessage(ArrayList<Socket> mySockets) {//конструктор класса
            this.mySockets = mySockets;
        }

        @Override
        public void run() {
            int id = 0;
            try (Scanner scanner = new Scanner(System.in)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] message = line.split(":");
                    id = Integer.parseInt(message[0].trim());
                    socket = mySockets.get(id);
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    out.writeUTF(line);
                }
            } catch (IOException ex) {
                System.out.println("Введены не верные данные. Подключение к клиенту разорвано. " + ex);
            } catch (NumberFormatException ex) {
                System.out.println("Необходимо следовать шаблону  'id-клиента : текст сообщения'  Ошибка:" + ex);
            }catch (IndexOutOfBoundsException ex){
                System.out.println("Клиент с таким id отсутствует. Ошибка "+ex);
            }
        }
    }

    static class ConnectClient implements Runnable {//класс для создания потока вновь подключенного клиента
        // и вывода клиентского сообщения
        private Socket socket;
        private int id;

        private ConnectClient(Socket socket, int id) {//конструктор класса
            this.socket = socket;
            this.id = id;
        }

        @Override
        public void run() {//метод вывода сообщения от клиента
            try (DataInputStream in = new DataInputStream(socket.getInputStream())) {

                while (true) {
                    try {
                        System.out.println(String.format("Новое сообщение от клиента %d> %s", id, in.readUTF()));
                    } catch (IOException ex) {
                        System.out.println(String.format("Клиент %d разорвал соединение", id));
                        break;
                    }
                }
            } catch (IOException ex) {
                System.out.println("Ошибка при подключении к сокету. " + ex);
            }
        }
    }

    public static void main(String[] args) {
        Socket socket;
        Thread threadClient;
        int counter = 0;
        ArrayList<Socket> mySockets = new ArrayList<>();
        Thread thread = new Thread(new OutputMessage(mySockets));//поток для отправки сообщения
        thread.setDaemon(true);
        thread.start();
        try (ServerSocket serverSocket = new ServerSocket(7777)) {
            System.out.println("Сервер ожидает подключения!");
            while (true) {

                System.out.println("Для ответа клиенту введите его id и через двоеточние текст сообщения");
                socket = serverSocket.accept();
                System.out.println(String.format("Кто-то подключился: %s, id клиента %d", socket.getInetAddress(), counter));
                mySockets.add(counter, socket);
                threadClient = new Thread(new ConnectClient(socket, counter));
                threadClient.setDaemon(true);
                threadClient.start();
                counter++;
            }
        } catch (IOException e) {
            System.out.println("Ошибка при открытии ServerSocket. " + e);
        }
    }
}

