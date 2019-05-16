package Java.ru.geekbrains.NetworkChat.Client;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

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

    public TextMessage(Date created, String userFrom, String text) {
        this.created = LocalDateTime.ofInstant(created.toInstant(), ZoneId.systemDefault());;
        this.userFrom = userFrom;
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

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public void setUserTo(String userTo) {
        this.userTo = userTo;
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
