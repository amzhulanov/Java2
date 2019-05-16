package Java.ru.geekbrains.NetworkChat.Client;

import Java.ru.geekbrains.NetworkChat.Client.TextMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MessagePatterns {

    public static final String AUTH_PATTERN = "/auth %s %s";
    public static final String AUTH_SUCCESS_RESPONSE = "/auth successful";
    public static final String AUTH_FAIL_RESPONSE = "/auth fail";
    public static final String AUTH_LOGIN_FAIL_RESPONSE = "/auth login fail";
    public static final String AUTH_LOGIN_NON_EXISTENT = "/auth login non existent";


    public static final String DISCONNECTED = "/disconnect";
    public static final String DISCONNECTED_SEND = DISCONNECTED +" %s";

    public static final String CONNECTED = "/connected";
    public static final String CONNECTED_SEND = CONNECTED + " %s";

    public static final String USER_LIST_TAG = "/user_list";
    public static final String USER_LIST_RESPONSE = USER_LIST_TAG + " %s";

    public static final String REQUEST="/req";

    public static final String MESSAGE_PREFIX = "/w";
    public static final String MESSAGE_SEND_PATTERN = MESSAGE_PREFIX + " %s %s %s";

    public static final Pattern MESSAGE_REC_PATTERN = Pattern.compile("^/w (\\w+) (.\\w+) (.+)", Pattern.MULTILINE);

    public static final String REG_PATTERN = "/reg %s %s";
    public static final String REG_LOGIN_BUSY="/reg_login_busy";
    public static final String REG_SUCCESS_RESPONSE="/reg successful";


    public static TextMessage parseTextMessageRegx(String text) {
        Matcher matcher = MESSAGE_REC_PATTERN.matcher(text);
        if (matcher.matches()) {
            return new TextMessage(matcher.group(1), matcher.group(2), matcher.group(3));//(usertTo,userFrom,text)
        } else {
            return null;
        }
    }

    public static TextMessage parseTextMessage(String text, String userTo) {
        String[] parts = text.split(" ", 3);
        if (parts.length == 3 && parts[0].equals(MESSAGE_PREFIX)) {
            return new TextMessage(parts[1], userTo, parts[2]);
        } else {
            return null;
        }
    }
    public static TextMessage parseHistoryFile(String text)   {
        String[] parts = text.split(" ", 5);
        if (parts.length == 5 ) {
            System.out.printf("parts[0]=%s, parts[2]=%s,parts[4]=%s",parts[0], parts[2],parts[4]);
                return new TextMessage(parts[0], parts[2],parts[4]);

        } else {
            return null;
        }
    }

    public static String parseConnectedMessage(String text) {//парсинг сообщения, что пользователь подключился
        String[] parts = text.split(" ");
        if (parts.length == 2 && parts[0].equals(String.format(CONNECTED))) {
            return parts[1];
        } else {
            return null;
        }
    }

    public static String parseDisconnectedMessage(String text) {//парсинг сообщения, что пользователь отключился
        String[] parts = text.split(" ");
        if (parts.length == 2 && parts[0].equals(String.format(DISCONNECTED))) {
            return parts[1];
        } else {
            return null;
        }
    }

    public static Set<String> parseUserList(String text) {
        String[] parts = text.split(" ");
        if (parts.length >= 1 && parts[0].equals(USER_LIST_TAG)) {
            Set<String> users = new HashSet<>();
            for (int i=1; i<parts.length; i++) {
                users.add(parts[i]);
            }
            return users;
        } else {
            return null;
        }
    }
}

