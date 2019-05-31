package Java.ru.geekbrains.NetworkChat.Server.EventLog;

import java.sql.*;
import java.util.logging.LogRecord;

import static Java.ru.geekbrains.NetworkChat.Server.ChatServer.logger;

public class EventLogJdbcImpl implements EventLog{
    private final Connection conn;
    private Statement statmt = null;


    public EventLogJdbcImpl(Connection conn) throws SQLException {//создаю таблицу пользователей, если её нет

        this.conn = conn;

        try {
            statmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        statmt.execute("CREATE TABLE if not exists log (id int primary key, type_msg varchar(25), datetime_msg datetime, text_msg text)");
        logger.fine("CREATE TABLE log");
    }

    @Override
    public void insertLog(LogRecord record) {
        try {
            PreparedStatement preparedStatement=conn.prepareStatement("insert into log (type_msg,datetime_msg,text_msg) values(?,?,?)");
            preparedStatement.setString(1,record.getLevel().getName());
            preparedStatement.setString(2,String.format("%tF %tT",record.getMillis(),record.getMillis()));
            preparedStatement.setString(3,record.getMessage());
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            logger.fine(String.format("insert into log: type=%s text=%s datetime=%s"
                                    ,record.getLevel().getName()
                                    ,record.getMessage()
                                    ,String.format("%tF %tT",record.getMillis())));
            e.printStackTrace();
        }
    }
}
