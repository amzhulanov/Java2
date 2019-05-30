package Java.ru.geekbrains.NetworkChat.Server.EventLog;

import java.util.logging.LogRecord;

public interface EventLog {
    void insertLog(LogRecord record);
}
