package Java.ru.geekbrains.NetworkChat.Client;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import static Java.ru.geekbrains.NetworkChat.Client.MessagePatterns.parseHistoryFile;
import static Java.ru.geekbrains.NetworkChat.Client.MessagePatterns.parseTextMessageRegx;

public class HistoryMessage {
    /*После авторизации пользователя
    Если нет папки HistoryMessage,    то папка создаётся
    Флаг наличия папки возводится в True
    Если есть файл history_login.txt, запускается метод считать100ПоследнихСтрок()
    Иначе создать файл history_login.txt

    При получении сообщения или отправке

    В файл добавляется время, логин и текст сообщения.

    При закрытии соединения файл закрывается

*/
    String PATH = "C:\\GeekBrains\\Faculty Java\\JavaCore2\\src\\Java\\ru\\geekbrains\\NetworkChat\\Client\\HistoryMessage";
    Path historyDir = Paths.get(PATH);

    public HistoryMessage() {//Если нет папки HistoryMessage,    то папка создаётся
        //Флаг наличия папки возводится в True


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


        File historyFile = new File(PATH, "history_" + login + ".txt");
        TextMessage textMessage;
        try (BufferedReader reader = new BufferedReader(new FileReader(historyFile))) {
            while (reader.ready()) {

                textMessage = parseHistoryFile(reader.readLine());//Разбиваю полученный текст. Определяю отправителя, получателя и текст
                messageReciever.submitMessage(textMessage);
            }


         //   String msg = "";
           /* //TextMessage textMessage;
            textMessage.setUserFrom();
            textMessage.setUserTo();
            textMessage.setCreated();
            textMessage.setText();*/


            //return msg;
        }
    }

    public void writeMessage(LocalDateTime created, String login,String userFrom,String textMessage) throws IOException {
        File historyFile=new File(PATH,"history_"+login+".txt");
        try(PrintWriter writer=new PrintWriter(new BufferedWriter(new FileWriter(historyFile,true)))) {
            writer.printf("%s : %s : %s%n",created,userFrom,textMessage);
        }



    }
}

