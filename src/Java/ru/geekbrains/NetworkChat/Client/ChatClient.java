package Java.ru.geekbrains.NetworkChat.Client;

import Java.ru.geekbrains.NetworkChat.Client.swing.ViewWindow;

import javax.swing.*;

public class ChatClient {


    private static ViewWindow viewWindow;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                viewWindow = new ViewWindow();

            }

        });
    }
}
