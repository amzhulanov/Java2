package Java.ru.geekbrains.NetworkChat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TextMessage {
    private String userFrom;
    private String userTo;
    private String text;

    public TextMessage(String userTo, String userFrom, String text) {
        this.created = LocalDateTime.now();
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

    }
