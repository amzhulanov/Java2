package Java.ru.geekbrains.lesson4.HomeWork4;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TextMessage {
    private String created;
    private String userName;
    private String text;

    public TextMessage(String userName, String text) {
        this.created = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")).toString();
        this.userName = userName;
        this.text = text;
    }

    public String getCreated() {
        return created;
    }

    public String getUserName() {
        return userName;
    }

    public String getText() {
        return text;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setText(String text) {
        this.text = text;
    }
}
