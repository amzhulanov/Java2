package Java.ru.geekbrains.NetworkChat;

import Java.ru.geekbrains.NetworkChat.swing.ViewWindow;

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
