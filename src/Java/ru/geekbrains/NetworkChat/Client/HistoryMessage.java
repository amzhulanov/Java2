package Java.ru.geekbrains.NetworkChat.Client;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

import static Java.ru.geekbrains.NetworkChat.Client.MessagePatterns.parseHistoryFile;
import static Java.ru.geekbrains.NetworkChat.Client.swing.TextMessageCellRenderer.dateTimeFormatter;

public class HistoryMessage {

    private final String PATH = "C:\\GeekBrains\\Faculty Java\\JavaCore2\\src\\Java\\ru\\geekbrains\\NetworkChat\\Client\\HistoryMessage";
    private final Path historyDir = Paths.get(PATH);

    public HistoryMessage() {
    }

    public Path createFolder() {
        if (!Files.exists(historyDir)) {
            try {
                return Files.createDirectory(historyDir);
            } catch (IOException e) {
                System.out.printf("Каталог %s не создан", historyDir);
                e.printStackTrace();
            }
        }
        return historyDir;
    }

    public void createFile(Path path,String login) {
        Path file=path.resolve("history_"+login+".txt");
        if (!Files.exists(file)){
            try {
                Path newFile=Files.createFile(file);
                System.out.printf("Файл %s создан",newFile);
            } catch (IOException e) {
                System.out.printf("Файл %s не создан");
                e.printStackTrace();
            }
        }
    }

    public void readMessage(String login,MessageReciever messageReciever) throws IOException {
        Map treeMap=new TreeMap<>();
        File historyFile = new File(PATH, "history_" + login + ".txt");

        int countRow=-1;//переменная для подсчёта строк, просто дял удобства
        try (BufferedReader reader = new BufferedReader(new FileReader(historyFile))) {
            while (reader.ready()) {
                treeMap.put(++countRow,reader.readLine());
            }
        }

        TextMessage textMessage;
        int rowOut=0; //чтобы в окне чата строки шли по дате от старых к новым
        if (countRow>=100){//определяю с какой строки выводить сообщение. Например, если всего 115 строк, то вывод пойдёт с 15-ой строки
            rowOut=countRow-100;
        }
        for (int count=rowOut;rowOut<=countRow;rowOut++){//вывод сообщений в окно пользователя
                       textMessage = parseHistoryFile(treeMap.get(count).toString());
            messageReciever.submitMessage(textMessage);
            count++;
        }
    }

    public void writeMessage(LocalDateTime created, String login,String userFrom,String textMessage) throws IOException {
        File historyFile=new File(PATH,"history_"+login+".txt");
        try(PrintWriter writer=new PrintWriter(new BufferedWriter(new FileWriter(historyFile,true)))) {
            writer.printf("%s : %s : %s%n",created.format(dateTimeFormatter),userFrom,textMessage);
        }
    }
}

