package Java.ru.geekbrains.NetworkChat;

public interface MessageReciever{
    void submitMessage(TextMessage message);

    void userConnected(String login);

    void userDisconnected(String login);
}
