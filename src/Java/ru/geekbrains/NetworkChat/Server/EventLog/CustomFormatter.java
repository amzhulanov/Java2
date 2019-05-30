package Java.ru.geekbrains.NetworkChat.Server.EventLog;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class CustomFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        return String.format("ChatServer: %s %tF %tT %s%n", record.getLevel(), record.getMillis(),record.getMillis(), record.getMessage());
    }

}
