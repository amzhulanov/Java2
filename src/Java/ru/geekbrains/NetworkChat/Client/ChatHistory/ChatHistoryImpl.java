package Java.ru.geekbrains.NetworkChat.Client.ChatHistory;

import Java.ru.geekbrains.NetworkChat.Client.TextMessage;

import java.io.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static Java.ru.geekbrains.NetworkChat.Client.MessagePatterns.parseHistoryFile;
import static Java.ru.geekbrains.NetworkChat.Client.swing.TextMessageCellRenderer.dateTimeFormatter;

public class ChatHistoryImpl implements ChatHistory {

    private static final String MESSAGE_PATTERN = "%s\t%s\t%s\t%s";
    private static final String HISTORY_FILE_NAME = "%s-message-history.txt";

    private final PrintWriter printWriter;
    private final File file;

    public ChatHistoryImpl(String login) throws IOException {
        file = new File(String.format(HISTORY_FILE_NAME, login));

        if (!file.exists()) {
            file.createNewFile();
        }
        printWriter = new PrintWriter(new BufferedOutputStream(new FileOutputStream(file, true)));
    }

    @Override
    public List<TextMessage> readMessage(int count) {
        Map treeMap = new TreeMap<>();
        int countRow = -1;//переменная для подсчёта строк, просто дял удобства
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                treeMap.put(++countRow, reader.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<TextMessage> res = new ArrayList<>();
        int rowOut = 0; //чтобы в окне чата строки шли по дате от старых к новым
        if (countRow >= count) {//определяю с какой строки выводить сообщение. Например, если всего 115 строк, то вывод пойдёт с 15-ой строки
            rowOut = countRow - count;
        }
        for (int i = rowOut; rowOut <= countRow; rowOut++) {//вывод сообщений в итоговый список строк

            res.add(parseHistoryFile(treeMap.get(i).toString()));
            i++;
        }
        return res;
    }

    @Override
    public void writeMessage(TextMessage textMessage) {
        String msg = String.format(MESSAGE_PATTERN, textMessage.getCreated().format(dateTimeFormatter), textMessage.getUserFrom(), textMessage.getUserTo(), textMessage.getText());
        printWriter.println(msg);
    }

    @Override
    public void flush() {
        printWriter.flush();
    }
}

