package Java.ru.geekbrains.NetworkChat.Client;

import java.time.LocalDateTime;

public class TextMessage {
    private String userFrom;
    private String userTo;
    private String text;
    private LocalDateTime created;

    public TextMessage(String userTo, String userFrom, String text) {
        this.created = LocalDateTime.now();
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.text = text;
    }

    public TextMessage(LocalDateTime created, String userTo,String userFrom, String text) {
        this.created = created;
        this.userTo=userTo;
        this.userFrom = userFrom;
        this.text = text;
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

    public LocalDateTime getCreated() {
        return created;
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


}
