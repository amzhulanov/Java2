package Java.ru.geekbrains.NetworkChat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TextMessage {
   // private String created;
    private String userFrom;
    private String userTo;
    private String text;

    public TextMessage(String userTo, String userFrom, String text) {
        this.created = LocalDateTime.now();//.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.text = text;
    }

    private LocalDateTime created;



    public String getUserFrom() {
        return userFrom;
    }

    public String getText() {
        return text;
    }

    public String getUserTo() {
        return userTo;
    }


    public void setUserFrom(String userFrom) {
        this.userFrom = userFrom;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    /**
     * Это не очень хорошее решение проблемы с использованием класса как на клиенте
     * так и на сервере, но ничего лучше пока не придумал
     */
   /* public void swapUsers() {
        String tmp = userFrom;
        userFrom = userTo;
        userTo = tmp;
    }*/
}
