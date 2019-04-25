package Java.ru.geekbrains.NetworkChat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TextMessage {
    private String created;
    private String userFrom;
    private String userTo;
    private String text;

    public TextMessage(String userTo, String userFrom, String text) {
        this.created = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")).toString();
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.text = text;
    }

    public String getCreated() {
        return created;
    }

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
}
