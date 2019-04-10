package Java.ru.geekbrains.lesson4.HomeWork4;

import static Java.ru.geekbrains.lesson4.HomeWork4.ViewWindow.*;
import javax.swing.*;

public class ChatController extends JFrame {
    //метод для обработки сообщения
    public static void sendText(String message) {
        if (message.length() > 0) {
            textArea.add(textArea.getSize(), message + "\n");


        }
    }
}
