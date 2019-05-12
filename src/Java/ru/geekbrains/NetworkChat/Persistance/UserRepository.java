package Java.ru.geekbrains.NetworkChat.Persistance;

import Java.ru.geekbrains.NetworkChat.ChatServer;
import Java.ru.geekbrains.NetworkChat.User;
import com.mysql.cj.protocol.Resultset;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final Connection conn;
    private Statement statmt = null;

    public UserRepository(Connection conn) throws SQLException {//создаю таблицу пользователей, если её нет

        this.conn = conn;

        try {
            statmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        statmt.execute("CREATE TABLE if not exists users (id int primary key, login varchar(25), password varchar(25));");
        System.out.println("Table ok.");
    }

    public void insert(User user) { //добавляю нового пользователя
        try {
            statmt.executeQuery("insert into users (login,password) values ('" + user.getLogin() + "','" + user.getPassword() + "');");
        } catch (SQLException e) {
            System.out.println("Пользователь не добавлен");
            e.printStackTrace();
        }
    }

    public User findByLogin(String login) throws SQLException {//ищу пользователя в БД по логину
        ResultSet resultSet = null;
        resultSet = statmt.executeQuery("select id,login,password from users where login='" + login + "'");
        if (resultSet.next()==true){
            return new User(resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3));
        }
            return null;
    }

    public List<User> getAllUsers() throws SQLException {//извлекаю из БД полный список пользователей и выгружаю в List
        List<User> userArrayList = new ArrayList();
        ResultSet resultSet = statmt.executeQuery("select id,login,password  from users ");
        while (resultSet.next()) {

            userArrayList.add(new User(resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3)));
        }
        return userArrayList;
    }
}