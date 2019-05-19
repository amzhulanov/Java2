package Java.ru.geekbrains.NetworkChat.Client.ChatHistory;

import Java.ru.geekbrains.NetworkChat.Client.TextMessage;

import java.util.List;

public interface ChatHistory {
    void writeMessage(TextMessage textMessage);

    List<TextMessage> readMessage(int count);

    void flush();
}
