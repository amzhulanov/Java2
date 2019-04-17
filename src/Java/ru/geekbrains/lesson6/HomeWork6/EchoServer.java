package Java.ru.geekbrains.lesson6.HomeWork6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class EchoServer {

    static class outputMessage implements Runnable {


        private Socket socket;

        public outputMessage(Socket socket) {

            this.socket = socket;
        }

        @Override
        public void run() {
            try (Scanner scanner = new Scanner(System.in))
            {
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    out.writeUTF(line);
                }
            } catch (
                    IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(7777)) {

            System.out.println("Сервер ожидает подключения!");
            Socket socket = serverSocket.accept();
            System.out.println("Кто-то подключился: " + socket.getInetAddress());

            DataInputStream in = new DataInputStream(socket.getInputStream());

            Thread thread = new Thread(new outputMessage(socket));//поток для чтения сообщения
            thread.start();
            while (true) {
                try {
                    System.out.println("Новое сообщение > " + in.readUTF());
                } catch (IOException ex) {
                    ex.printStackTrace();
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace(); 
        }
    }
}
