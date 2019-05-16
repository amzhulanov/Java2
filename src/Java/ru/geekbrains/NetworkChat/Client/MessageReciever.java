package Java.ru.geekbrains.NetworkChat.Client;

import Java.ru.geekbrains.NetworkChat.Client.TextMessage;

import java.util.Set;

public interface MessageReciever{
    void submitMessage(TextMessage message);

    void userConnected(String login);

    void userDisconnected(String login);

    void updateUserList(Set<String> users);
}
