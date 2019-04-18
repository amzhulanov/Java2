package Java.ru.geekbrains.lesson6.HomeWork6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {
    static class InputMessage implements Runnable {

        private Socket socket;


        private InputMessage(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {//метод вывода сообщений от сервера
            try {
                DataInputStream in = new DataInputStream(socket.getInputStream());
                while (true) {
                    try {
                        System.out.printf("%nНовое сообщение от сервера>  " + in.readUTF() + "%n");
                        System.out.print("Введите сообщение > ");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (IOException ex) {
                System.out.println("Не удалось прочитать входящий поток" + ex);
            }
        }
    }

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in);
             Socket socket = new Socket("localhost", 7777)) {

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            Thread thread = new Thread(new InputMessage(socket));//поток для чтения сообщения
            thread.setDaemon(true);
            thread.start();

            do {
                System.out.print("Введите сообщение > ");
                String line = scanner.nextLine();
                out.writeUTF(line);//отправляем сообщение на сервер

            } while (scanner.hasNextLine());
        } catch (IOException ex) {
            System.out.println("Сервер не обнаружен. " + ex);
        }
    }
}