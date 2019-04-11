package Java.ru.geekbrains.lesson4.HomeWork4;

import javax.swing.*;

public class Chat {

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
