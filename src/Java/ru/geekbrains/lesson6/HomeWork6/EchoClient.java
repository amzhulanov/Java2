package Java.ru.geekbrains.lesson6.HomeWork6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {
    static class inputMessage implements Runnable {

        private Socket socket;

        //private inputMessage(Socket socket) {
        private inputMessage(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                DataInputStream in = new DataInputStream(socket.getInputStream());
                while (true) {
                    try {
                        System.out.printf("%nНовое сообщение > " + in.readUTF());
                        System.out.print("Введите сообщение > ");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in);
             Socket socket = new Socket("localhost", 7777)) {

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            Thread thread = new Thread(new inputMessage(socket));//поток для чтения сообщения
            thread.start();

            do {
                System.out.print("Введите сообщение > ");
                String line = scanner.nextLine();
                out.writeUTF(line);//отправляем сообщение на сервер

            } while (scanner.hasNextLine());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}